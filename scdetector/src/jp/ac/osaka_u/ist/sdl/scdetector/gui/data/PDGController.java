package jp.ac.osaka_u.ist.sdl.scdetector.gui.data;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;


/**
 * çÏê¨ÇµÇΩPDGÇä«óùÇ∑ÇÈÇΩÇﬂÇÃÉNÉâÉX
 * 
 * @author higo
 *
 */
public class PDGController {

    private PDGController() {
        this.unitToPDG = new ConcurrentHashMap<CallableUnitInfo, PDG>();
        this.nodeToPDG = new ConcurrentHashMap<PDGNode<?>, PDG>();
    }

    public void put(final CallableUnitInfo key, final PDG value) {

        if (null == key || null == value) {
            throw new IllegalArgumentException();
        }

        this.unitToPDG.put(key, value);

        for (final PDGNode<?> node : value.getAllNodes()) {
            this.nodeToPDG.put(node, value);
        }
    }

    public PDG getPDG(final CallableUnitInfo unit) {
        return this.unitToPDG.get(unit);
    }

    public PDG getPDG(final PDGNode<?> node) {
        return this.nodeToPDG.get(node);
    }

    public Set<Map.Entry<CallableUnitInfo, PDG>> entrySet() {
        return Collections.unmodifiableSet(this.unitToPDG.entrySet());
    }

    public Collection<PDG> getPDGs() {
        return Collections.unmodifiableCollection(this.unitToPDG.values());
    }

    public Set<PDG> getPDGs(final String filename) {

        final Set<PDG> pdgs = new HashSet<PDG>();
        for (final Map.Entry<CallableUnitInfo, PDG> entry : this.unitToPDG.entrySet()) {

            final CallableUnitInfo ownerMethod = entry.getKey();
            final TargetClassInfo ownerClass = (TargetClassInfo) ownerMethod.getOwnerClass();
            if (ownerClass.getOwnerFile().getName().equals(filename)) {
                pdgs.add(entry.getValue());
            }
        }

        return Collections.unmodifiableSet(pdgs);
    }

    private final ConcurrentMap<CallableUnitInfo, PDG> unitToPDG;

    private final ConcurrentMap<PDGNode<?>, PDG> nodeToPDG;

    /**
     * éwíËÇ≥ÇÍÇΩPDGControllerÇï‘Ç∑
     * 
     * @param id
     * @return
     */
    public static PDGController getInstance(final String id) {

        PDGController controller = map.get(id);
        if (null == controller) {
            controller = new PDGController();
            map.put(id, controller);
        }

        return controller;
    }

    private static final Map<String, PDGController> map = new HashMap<String, PDGController>();
}
