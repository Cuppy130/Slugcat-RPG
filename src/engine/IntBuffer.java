package engine;

public class IntBuffer {
    private int data;
    private int offset;
    private static final int max = 32;
    // Constructors
    public IntBuffer() {
        this(0);
    }

    public IntBuffer(int i) {
        data = i;
        offset = 0;
    }

    public void writeBool(boolean... args) {
        for (boolean b : args) {
            if (offset <= max) {
                data |= (b ? 1 : 0) << offset;
                offset++;
            } else {
                throw new IllegalStateException("Exceeded 32-bit limit");
            }
        }
    }
    
    public void writeInt4(int... ints) {
        for (int i : ints) {
            if (offset + 4 <= max) {
                data |= (long) (i & 0xF) << offset;
                offset += 4;
            } else {
                throw new IllegalStateException("Exceeded 32-bit limit");
            }
        }
    }

    public void writeInt8(int... ints) {
        for (int i : ints) {
            if (offset + 8 <= max) {
                data |= (long) (i & 0xFF) << offset;
                offset += 8;
            } else {
                throw new IllegalStateException("Exceeded 32-bit limit");
            }
        }
    }

    public void writeColor(int r, int g, int b) {
        if (offset + 12 <= max) {
            r = r & 0xF;
            g = g & 0xF;
            b = b & 0xF;
            data |= ((r << 8) | (g << 4) | b) << (max - offset - 12);
            offset += 12;
        } else {
            throw new IllegalStateException("Exceeded 32-bit limit");
        }
    }
    
    

    public boolean readBool() {
        if (offset < max) {
            boolean b = (data & (1 << offset)) != 0;
            offset++;
            return b;
        } else {
            throw new IllegalArgumentException("Bit position exceeds 32-bit range");
        }
    }

    public int readInt4() {
        if (offset + 4 <= max) {
            int dat = (int) ((data >> offset) & 0xF);
            offset+=4;
            return dat;
        } else {
            throw new IllegalArgumentException("Bit position exceeds 32-bit range");
        }
    }

    public int readInt8() {
        if (offset + 8 <= max) {
            int dat = (int) ((data >> offset) & 0xFF);
            offset+=8;
            return dat;
        } else {
            throw new IllegalArgumentException("Bit position exceeds 32-bit range");
        }
    }

    public int readColor() {
        if (offset + 12 <= max) {
            int colorData = (data >> (max - offset - 12)) & 0xFFF;
            int r = (colorData >> 8) & 0xF;
            int g = (colorData >> 4) & 0xF;
            int b = colorData & 0xF;
            offset += 12;
            return (r << 8) | (g << 4) | b;
        } else {
            throw new IllegalArgumentException("Bit position exceeds 32-bit range");
        }
    }
    

    public void reset() {
        data = 0;
        offset = 0;
    }

    public int value() {
        return data;
    }

    public int bitsUsed(){
        return offset;
    }

    public int limit(){
        return max;
    }
}
