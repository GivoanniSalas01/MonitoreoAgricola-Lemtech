package com.monitoreolemtech.demo.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.monitoreolemtech.demo.dto.CampoResumenDto;
import com.monitoreolemtech.demo.dto.NodoDetalleDto;
import com.monitoreolemtech.demo.model.Empresa;
import com.monitoreolemtech.demo.model.Nodo;
import com.monitoreolemtech.demo.model.SectorCampo;
import com.monitoreolemtech.demo.repository.EmpresaRepository;
import com.monitoreolemtech.demo.repository.NodoRepository;
import com.monitoreolemtech.demo.repository.SectorCampoRepository;
import com.monitoreolemtech.demo.service.CampoService;

@Service
public class CampoServiceImpl implements CampoService {

    private final SectorCampoRepository sectorCampoRepository;
    private final NodoRepository nodoRepository;
    private final EmpresaRepository empresaRepository;

    private final List<Integer> tiposEquipo;
    private final long minutosSinSenal;

    // id_empresa de las empresas a mostrar; vacío = todas
    private final Set<Long> empresasVisibles;

    public CampoServiceImpl(SectorCampoRepository sectorCampoRepository,
                            NodoRepository nodoRepository,
                            EmpresaRepository empresaRepository,
                            @Value("${monitoreo.tipos-equipo:0,1,3,5,6,7,8}") List<Integer> tiposEquipo,
                            @Value("${monitoreo.minutos-sin-senal:1440}") long minutosSinSenal,
                            @Value("${monitoreo.empresas-visibles:}") List<Long> empresasVisibles) {
        this.sectorCampoRepository = sectorCampoRepository;
        this.nodoRepository = nodoRepository;
        this.empresaRepository = empresaRepository;
        this.tiposEquipo = tiposEquipo;
        this.minutosSinSenal = minutosSinSenal;
        this.empresasVisibles = Set.copyOf(empresasVisibles);
    }

    // Datos ya cargados y listos para armar el resumen o el detalle
    private record Contexto(Map<Long, Empresa> empresas,
                            Map<Integer, SectorCampo> sectores,
                            Map<Integer, Long> empresaPorSector,
                            List<Nodo> nodos,
                            LocalDateTime ahora,
                            LocalDateTime limite) {
    }

    private Contexto cargarContexto() {
        Map<Long, Empresa> empresas = empresaRepository.findAll().stream()
                .collect(Collectors.toMap(Empresa::getIdEmpresa, e -> e, (a, b) -> a));

        Map<Integer, SectorCampo> sectores = sectorCampoRepository.findAll().stream()
                .collect(Collectors.toMap(SectorCampo::getIdSector, s -> s, (a, b) -> a));

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = ahora.minusMinutes(minutosSinSenal);

        // Equipos de los tipos monitoreados que están operativos o están reportando
        List<Nodo> nodos = nodoRepository.findByTipoNodoIn(tiposEquipo).stream()
                .filter(n -> n.getIdSector() != null)
                .filter(n -> Boolean.TRUE.equals(n.getOperativa()) || estaReportando(n, limite))
                .collect(Collectors.toList());

        // Empresa de cada sector: la del sector; si no existe, la más frecuente entre sus nodos
        Map<Integer, List<Nodo>> nodosPorSector = nodos.stream()
                .collect(Collectors.groupingBy(Nodo::getIdSector));

        Map<Integer, Long> empresaPorSector = new HashMap<>();
        nodosPorSector.forEach((idSector, lista) -> {
            SectorCampo sector = sectores.get(idSector);
            Long idEmpresa = (sector != null && sector.getIdEmpresa() != null)
                    ? sector.getIdEmpresa()
                    : masFrecuente(lista, Nodo::getIdEmpresa);
            if (idEmpresa != null) {
                empresaPorSector.put(idSector, idEmpresa);
            }
        });

        return new Contexto(empresas, sectores, empresaPorSector, nodos, ahora, limite);
    }

    // Una empresa se muestra si está en la lista (o la lista está vacía) y no es morosa
    private boolean empresaVisible(Long idEmpresa, Map<Long, Empresa> empresas) {
        if (idEmpresa == null) {
            return false;
        }
        if (!empresasVisibles.isEmpty() && !empresasVisibles.contains(idEmpresa)) {
            return false;
        }
        Empresa empresa = empresas.get(idEmpresa);
        return empresa == null
                || empresa.getMoroso() == null
                || !"si".equalsIgnoreCase(empresa.getMoroso().trim());
    }

    @Override
    public List<CampoResumenDto> obtenerResumenCampos() {

        Contexto ctx = cargarContexto();

        // Una tarjeta por empresa: se suman todos sus sectores
        Map<Long, List<Nodo>> nodosPorEmpresa = ctx.nodos().stream()
                .filter(n -> ctx.empresaPorSector().containsKey(n.getIdSector()))
                .collect(Collectors.groupingBy(n -> ctx.empresaPorSector().get(n.getIdSector())));

        List<CampoResumenDto> resultado = new ArrayList<>();

        for (Map.Entry<Long, List<Nodo>> entrada : nodosPorEmpresa.entrySet()) {
            Long idEmpresa = entrada.getKey();
            if (!empresaVisible(idEmpresa, ctx.empresas())) {
                continue;
            }

            List<Nodo> nodosEmpresa = entrada.getValue();
            Map<Integer, List<Nodo>> porSector = nodosEmpresa.stream()
                    .collect(Collectors.groupingBy(Nodo::getIdSector));

            // Sectores ordenados por cantidad de equipos (el mayor primero)
            List<Map.Entry<Integer, List<Nodo>>> sectoresOrdenados = porSector.entrySet().stream()
                    .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
                    .collect(Collectors.toList());

            List<String> codigos = new ArrayList<>();
            List<String> nombresSectores = new ArrayList<>();
            SectorCampo sectorPrincipal = null;

            for (Map.Entry<Integer, List<Nodo>> sectorEntrada : sectoresOrdenados) {
                SectorCampo sector = ctx.sectores().get(sectorEntrada.getKey());
                String codigo = codigoDe(sector, sectorEntrada.getValue());
                String nombreSector = nombreDe(sector, codigo, sectorEntrada.getKey());

                if (codigo != null) {
                    codigos.add(codigo);
                }
                nombresSectores.add(nombreSector);
                if (sectorPrincipal == null && sector != null) {
                    sectorPrincipal = sector;
                }
            }

            Empresa empresa = ctx.empresas().get(idEmpresa);
            String nombreEmpresa = (empresa != null && empresa.getNombre() != null && !empresa.getNombre().isBlank())
                    ? empresa.getNombre().trim()
                    : nombresSectores.get(0);

            long total = nodosEmpresa.size();
            long caidos = nodosEmpresa.stream().filter(n -> estaCaido(n, ctx.limite())).count();
            double porcentaje = total == 0 ? 0 : caidos * 100.0 / total;

            resultado.add(new CampoResumenDto(
                    idEmpresa,
                    nombreEmpresa,
                    String.join(" · ", codigos),
                    String.join(", ", nombresSectores),
                    porSector.size(),
                    sectorPrincipal != null ? sectorPrincipal.getRegion() : null,
                    sectorPrincipal != null ? sectorPrincipal.getComuna() : null,
                    total,
                    caidos,
                    porcentaje,
                    calcularColor(caidos, porcentaje)));
        }

        resultado.sort(Comparator.comparing(CampoResumenDto::getNombre,
                Comparator.nullsLast(String::compareToIgnoreCase)));

        return resultado;
    }

    @Override
    public List<NodoDetalleDto> obtenerNodosDeEmpresa(Long idEmpresa) {

        Contexto ctx = cargarContexto();

        if (!empresaVisible(idEmpresa, ctx.empresas())) {
            return List.of();
        }

        return ctx.nodos().stream()
                .filter(n -> idEmpresa.equals(ctx.empresaPorSector().get(n.getIdSector())))
                .map(n -> {
                    SectorCampo sector = ctx.sectores().get(n.getIdSector());
                    String codigoSector = (sector != null && sector.getIdCampoLaudus() != null)
                            ? sector.getIdCampoLaudus()
                            : n.getIdCampoLaudus();
                    String nombreSector = (sector != null && sector.getNombre() != null && !sector.getNombre().isBlank())
                            ? sector.getNombre().trim()
                            : null;
                    Long minutos = n.getFechaUltimoM() == null
                            ? null
                            : Duration.between(n.getFechaUltimoM(), ctx.ahora()).toMinutes();

                    return new NodoDetalleDto(
                            n.getIdNodo(),
                            n.getIdSector(),
                            codigoSector,
                            nombreSector,
                            n.getNombre(),
                            n.getTipoNodo(),
                            n.getCategoria(),
                            n.getConectividad(),
                            n.getFechaUltimoM(),
                            minutos,
                            estaCaido(n, ctx.limite()) ? "OFFLINE" : "ONLINE");
                })
                // por sector; dentro de cada sector, primero los caídos y luego por nombre
                .sorted(Comparator
                        .comparing((NodoDetalleDto d) -> d.getNombreSector() != null
                                ? d.getNombreSector()
                                : String.valueOf(d.getCodigoSector()), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(NodoDetalleDto::getIdSector)
                        .thenComparing((NodoDetalleDto d) -> !"OFFLINE".equals(d.getEstado()))
                        .thenComparing(NodoDetalleDto::getNombre, Comparator.nullsLast(String::compareToIgnoreCase)))
                .collect(Collectors.toList());
    }

    // Código del sector; si el sector no existe, el código más frecuente entre sus nodos
    private String codigoDe(SectorCampo sector, List<Nodo> nodos) {
        if (sector != null && sector.getIdCampoLaudus() != null) {
            return sector.getIdCampoLaudus();
        }
        return masFrecuente(nodos, Nodo::getIdCampoLaudus);
    }

    // Nombre del sector; si no tiene, su código; si tampoco, "Sector N"
    private String nombreDe(SectorCampo sector, String codigo, Integer idSector) {
        if (sector != null && sector.getNombre() != null && !sector.getNombre().isBlank()) {
            return sector.getNombre().trim();
        }
        return codigo != null ? codigo : "Sector " + idSector;
    }

    // Valor que más se repite entre los nodos (ignora nulos)
    private <T> T masFrecuente(List<Nodo> nodos, Function<Nodo, T> campo) {
        return nodos.stream()
                .map(campo)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Reportando = transmitió dentro del límite de tiempo
    private boolean estaReportando(Nodo nodo, LocalDateTime limite) {
        return nodo.getFechaUltimoM() != null && !nodo.getFechaUltimoM().isBefore(limite);
    }

    // Un equipo está caído si nunca transmitió o su última transmisión es anterior al límite
    private boolean estaCaido(Nodo nodo, LocalDateTime limite) {
        return nodo.getFechaUltimoM() == null || nodo.getFechaUltimoM().isBefore(limite);
    }

    // Verde sin caídos, rojo con 25% o más, amarillo en el resto
    private String calcularColor(long caidos, double porcentaje) {
        if (caidos == 0) {
            return "VERDE";
        }
        if (porcentaje >= 25) {
            return "ROJO";
        }
        return "AMARILLO";
    }
}
