package mdk.mop.utils;

public class Hash {
    private int countBit0;
    private int countBit1;
    private int countByte;
    public Hash() {

    }

    public void Init() {
        countBit0 = 0;
        countBit1 = 0;
        countByte = 0;
    }

    public void bitHash(String data) {
        for (char c : data.toCharArray()) {
            for (int i = 0; i < 16; i++) {
                int bit = (c >> i) & 1;
                if (bit == 1) {
                    countBit1++;
                } else {
                    countBit0++;
                }
                countByte++;
            }
        }
    }

    public int hash(String value) {
        int hash = 0;
        int h = (countBit0 + countBit1 + countByte) % Short.MAX_VALUE * 2;
        if (value.length() > 0) {
            char val[] = value.toCharArray();

            for (int i = 0; i < val.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return hash;
    }
}
