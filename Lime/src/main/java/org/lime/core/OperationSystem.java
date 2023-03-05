package org.lime.core;

import java.util.Locale;

public class OperationSystem {

    public enum Vendor {
        Windows,
        MacOS,
        Linux
    }

    private static Vendor operationSystemVendor = null;

    public static Vendor getVendor() {
        if (operationSystemVendor == null) {
            String operationSystem = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((operationSystem.contains("mac")) || (operationSystem.contains("darwin"))) {
                operationSystemVendor = Vendor.MacOS;
            } else if (operationSystem.contains("win")) {
                operationSystemVendor = Vendor.Windows;
            } else if (operationSystem.contains("nux")) {
                operationSystemVendor = Vendor.Linux;
            }
        }
        return operationSystemVendor;
    }
}
