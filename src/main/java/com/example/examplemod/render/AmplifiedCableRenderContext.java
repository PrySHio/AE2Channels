package com.example.examplemod.render;

public final class AmplifiedCableRenderContext {

    private static final ThreadLocal<Integer> CABLE_TYPE =
            ThreadLocal.withInitial(() -> 0);

    private AmplifiedCableRenderContext() {
    }

    public static void setCableType(int type) {
        CABLE_TYPE.set(type);
    }

    public static int getCableType() {
        return CABLE_TYPE.get();
    }

    public static void clear() {
        CABLE_TYPE.remove();
    }
}