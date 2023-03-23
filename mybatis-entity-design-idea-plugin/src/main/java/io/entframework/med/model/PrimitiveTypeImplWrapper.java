/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

public class PrimitiveTypeImplWrapper extends JavaTypeImpl {

    private static PrimitiveTypeImplWrapper booleanInstance;

    private static PrimitiveTypeImplWrapper byteInstance;

    private static PrimitiveTypeImplWrapper characterInstance;

    private static PrimitiveTypeImplWrapper doubleInstance;

    private static PrimitiveTypeImplWrapper floatInstance;

    private static PrimitiveTypeImplWrapper integerInstance;

    private static PrimitiveTypeImplWrapper longInstance;

    private static PrimitiveTypeImplWrapper shortInstance;

    private final String toPrimitiveMethod;

    /**
     * Use the static getXXXInstance methods to gain access to one of the type wrappers.
     *
     * @param fullyQualifiedName fully qualified name of the wrapper type
     * @param toPrimitiveMethod  the method that returns the wrapped primitive
     */
    private PrimitiveTypeImplWrapper(String fullyQualifiedName, String toPrimitiveMethod) {
        super(fullyQualifiedName);
        this.toPrimitiveMethod = toPrimitiveMethod;
    }

    public String getToPrimitiveMethod() {
        return toPrimitiveMethod;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PrimitiveTypeImplWrapper)) {
            return false;
        }

        PrimitiveTypeImplWrapper other = (PrimitiveTypeImplWrapper) obj;

        return getFullyQualifiedName().equals(other.getFullyQualifiedName());
    }

    @Override
    public int hashCode() {
        return getFullyQualifiedName().hashCode();
    }

    public static PrimitiveTypeImplWrapper getBooleanInstance() {
        if (booleanInstance == null) {
            booleanInstance = new PrimitiveTypeImplWrapper("java.lang.Boolean", //$NON-NLS-1$
                    "booleanValue()"); //$NON-NLS-1$
        }

        return booleanInstance;
    }

    public static PrimitiveTypeImplWrapper getByteInstance() {
        if (byteInstance == null) {
            byteInstance = new PrimitiveTypeImplWrapper("java.lang.Byte", //$NON-NLS-1$
                    "byteValue()"); //$NON-NLS-1$
        }

        return byteInstance;
    }

    public static PrimitiveTypeImplWrapper getCharacterInstance() {
        if (characterInstance == null) {
            characterInstance = new PrimitiveTypeImplWrapper("java.lang.Character", //$NON-NLS-1$
                    "charValue()"); //$NON-NLS-1$
        }

        return characterInstance;
    }

    public static PrimitiveTypeImplWrapper getDoubleInstance() {
        if (doubleInstance == null) {
            doubleInstance = new PrimitiveTypeImplWrapper("java.lang.Double", //$NON-NLS-1$
                    "doubleValue()"); //$NON-NLS-1$
        }

        return doubleInstance;
    }

    public static PrimitiveTypeImplWrapper getFloatInstance() {
        if (floatInstance == null) {
            floatInstance = new PrimitiveTypeImplWrapper("java.lang.Float", //$NON-NLS-1$
                    "floatValue()"); //$NON-NLS-1$
        }

        return floatInstance;
    }

    public static PrimitiveTypeImplWrapper getIntegerInstance() {
        if (integerInstance == null) {
            integerInstance = new PrimitiveTypeImplWrapper("java.lang.Integer", //$NON-NLS-1$
                    "intValue()"); //$NON-NLS-1$
        }

        return integerInstance;
    }

    public static PrimitiveTypeImplWrapper getLongInstance() {
        if (longInstance == null) {
            longInstance = new PrimitiveTypeImplWrapper("java.lang.Long", //$NON-NLS-1$
                    "longValue()"); //$NON-NLS-1$
        }

        return longInstance;
    }

    public static PrimitiveTypeImplWrapper getShortInstance() {
        if (shortInstance == null) {
            shortInstance = new PrimitiveTypeImplWrapper("java.lang.Short", //$NON-NLS-1$
                    "shortValue()"); //$NON-NLS-1$
        }

        return shortInstance;
    }

}
