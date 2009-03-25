package jp.ac.osaka_u.ist.sel.metricstool.pdg;


public class IntraProceduralSlicer {

    /*
    private final IntraProceduralPDG pdg;

    public IntraProceduralSlicer(final TargetMethodInfo method) {
        if (null == method) {
            throw new IllegalArgumentException("method is null.");
        }

        this.pdg = new IntraProceduralPDG(method);
    }

    public Set<PDGNode<?>> extratForwardSlice(final VariableInfo<? extends UnitInfo> variable) {

        final PDGNode<?> startNode = this.getStartNode(variable);
        if (null == startNode) {
            return Collections.EMPTY_SET;
        }

        final Set<PDGNode<?>> slice = new HashSet<PDGNode<?>>();
        if (startNode instanceof ControllableNode) {
            slice.add((ControllableNode<?>) startNode);
        }

        for (final PDGEdge forwardEdge : startNode.getForwardEdges()) {
            this.buildForwardSlice(slice, forwardEdge.getToNode());
        }

        return Collections.unmodifiableSet(slice);
    }

    private void buildForwardSlice(final Set<PDGNode<?>> buildingSlice, final PDGNode<?> newNode) {

        boolean unBuilt = buildingSlice.add(newNode);
        if (!unBuilt) {
            return;
        }

        for (final PDGEdge forwardEdge : newNode.getForwardEdges()) {
            this.buildForwardSlice(buildingSlice, forwardEdge.getToNode());
        }
    }

    public Set<PDGNode<?>> extratBackwardSlice(final VariableInfo<? extends UnitInfo> variable) {
        final Set<PDGNode<?>> slice = new HashSet<PDGNode<?>>();

        for (final PDGNode<?> node : this.pdg.getAllNodes()) {
            if (node.getDefinedVariables().contains(variable)) {
                this.buildBackwardSlice(slice, node);
            }
        }

        return Collections.unmodifiableSet(slice);
    }

    private void buildBackwardSlice(final Set<PDGNode<?>> buildingSlice, final PDGNode<?> newNode) {

        if (newNode instanceof ControllableNode) {
            boolean unBuilt = buildingSlice.add((ControllableNode<?>) newNode);
            if (!unBuilt) {
                return;
            }
        }

        for (final PDGEdge backwardEdge : newNode.getBackwardEdges()) {
            this.buildBackwardSlice(buildingSlice, backwardEdge.getFromNode());
        }
    }

    public Set<PDGNode<?>> extratMetricSlice(final VariableInfo<? extends UnitInfo> variable) {
        final Set<PDGNode<?>> slice = new HashSet<PDGNode<?>>();
        slice.addAll(this.extratForwardSlice(variable));
        slice.addAll(this.extratBackwardSlice(variable));
        return Collections.unmodifiableSet(slice);
    }

    public IntraProceduralPDG getPDG() {
        return this.pdg;
    }

    private PDGNode<?> getStartNode(final VariableInfo<? extends UnitInfo> variable) {
        if (variable instanceof LocalVariableInfo) {
            return this.pdg.getNode(((LocalVariableInfo) variable).getDeclarationStatement());
        } else if (variable instanceof ParameterInfo) {
            return this.pdg.getNode((ParameterInfo) variable);
        }

        return null;
    }
    */
}
