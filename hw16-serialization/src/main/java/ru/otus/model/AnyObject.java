package ru.otus.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class AnyObject {
    public byte fieldByte;
    public short fieldShort;
    public int fieldInt;
    public Long fieldLong;
    public boolean fieldBoolean;
    public Float fieldFloat;
    public double fieldDouble;
    public static final transient String fieldStringNotSerialize = "DoNotSerialize";
    public String fieldString;
    public int[] fieldIntArray;
    public String[] fieldStringArray;
    public Collection<Integer> fieldCollection;

    public AnyObject() {
    }

    public AnyObject(byte fieldByte, short fieldShort, int fieldInt, Long fieldLong, boolean fieldBoolean, Float fieldFloat, double fieldDouble, String fieldString, int[] fieldIntArray, String[] fieldStringArray, Collection<Integer> fieldCollection) {
        this.fieldByte = fieldByte;
        this.fieldInt = fieldInt;
        this.fieldShort = fieldShort;
        this.fieldLong = fieldLong;
        this.fieldBoolean = fieldBoolean;
        this.fieldFloat = fieldFloat;
        this.fieldDouble = fieldDouble;
        this.fieldString = fieldString;
        this.fieldIntArray = fieldIntArray;
        this.fieldStringArray = fieldStringArray;
        this.fieldCollection = fieldCollection;
    }

    public Builder toBuilder() {
        return new Builder(fieldByte, fieldShort, fieldInt, fieldLong, fieldBoolean, fieldFloat, fieldDouble, fieldString, fieldIntArray, fieldStringArray, fieldCollection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnyObject anyObject = (AnyObject) o;
        return fieldByte == anyObject.fieldByte &&
                fieldShort == anyObject.fieldShort &&
                fieldInt == anyObject.fieldInt &&
                fieldBoolean == anyObject.fieldBoolean &&
                Objects.equals(fieldLong, anyObject.fieldLong) &&
                Objects.equals(fieldFloat, anyObject.fieldFloat) &&
                Objects.equals(fieldDouble, anyObject.fieldDouble) &&
                Objects.equals(fieldString, anyObject.fieldString) &&
                Arrays.equals(fieldIntArray, anyObject.fieldIntArray) &&
                Arrays.equals(fieldStringArray, anyObject.fieldStringArray) &&
                Objects.equals(fieldCollection, anyObject.fieldCollection);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fieldByte, fieldShort, fieldInt, fieldLong, fieldBoolean, fieldFloat, fieldDouble, fieldString, fieldCollection);
        result = 31 * result + Arrays.hashCode(fieldIntArray);
        result = 31 * result + Arrays.hashCode(fieldStringArray);
        return result;
    }

    public static class Builder {
        private byte fieldByte;
        private short fieldShort;
        private int fieldInt;
        private Long fieldLong;
        private boolean fieldBoolean;
        private Float fieldFloat;
        private double fieldDouble;
        private String fieldString;
        private int[] fieldIntArray;
        private String[] fieldStringArray;
        private Collection<Integer> fieldCollection;

        public Builder(byte fieldByte, short fieldShort, int fieldInt, Long fieldLong, boolean fieldBoolean, Float fieldFloat, double fieldDouble, String fieldString, int[] fieldIntArray, String[] fieldStringArray, Collection<Integer> fieldCollection) {
            this.fieldByte = fieldByte;
            this.fieldInt = fieldInt;
            this.fieldShort = fieldShort;
            this.fieldLong = fieldLong;
            this.fieldBoolean = fieldBoolean;
            this.fieldFloat = fieldFloat;
            this.fieldDouble = fieldDouble;
            this.fieldString = fieldString;
            this.fieldIntArray = fieldIntArray;
            this.fieldStringArray = fieldStringArray;
            this.fieldCollection = fieldCollection;
        }

        public Builder fieldByte(byte fieldByte) {
            this.fieldByte = fieldByte;
            return this;
        }

        public Builder fieldInt(int fieldInt) {
            this.fieldInt = fieldInt;
            return this;
        }

        public Builder fieldShort(short fieldShort) {
            this.fieldShort = fieldShort;
            return this;
        }

        public Builder fieldLong(Long fieldLong) {
            this.fieldLong = fieldLong;
            return this;
        }

        public Builder fieldBoolean(boolean fieldBoolean) {
            this.fieldBoolean = fieldBoolean;
            return this;
        }

        public Builder fieldFloat(Float fieldFloat) {
            this.fieldFloat = fieldFloat;
            return this;
        }

        public Builder fieldDouble(double fieldDouble) {
            this.fieldDouble = fieldDouble;
            return this;
        }

        public Builder fieldString(String fieldString) {
            this.fieldString = fieldString;
            return this;
        }

        public Builder fieldIntArray(int[] fieldIntArray) {
            this.fieldIntArray = fieldIntArray;
            return this;
        }

        public Builder fieldStringArray(String[] fieldStringArray) {
            this.fieldStringArray = fieldStringArray;
            return this;
        }

        public Builder fieldCollection(Collection<Integer> collection) {
            this.fieldCollection = collection;
            return this;
        }

        public AnyObject build() {
            return new AnyObject(fieldByte, fieldShort, fieldInt, fieldLong, fieldBoolean, fieldFloat, fieldDouble, fieldString, fieldIntArray, fieldStringArray, fieldCollection);
        }
    }
}