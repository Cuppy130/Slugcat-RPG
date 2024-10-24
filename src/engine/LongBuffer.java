package engine;

public class LongBuffer {
    private long data = 0L;
    private int offset = 0;
    private static final int max = 64;
    // Constructors
    public LongBuffer() {
        this(0L);
    }

    public LongBuffer(long i) {
        data = i;
    }

    public void writeBool(boolean... args) {
        for (boolean b : args) {
            if (offset <= max) {
                data |= (b ? 1L : 0L) << offset;
                offset++;
            } else {
                throw new IllegalStateException("Exceeded 64-bit limit");
            }
        }
    }
    
    public void writeInt4(int... ints) {
        for (int i : ints) {
            if (offset + 4 <= max) {
                data |= (long) (i & 0xF) << offset;
                offset += 4;
            } else {
                throw new IllegalStateException("Exceeded 64-bit limit");
            }
        }
    }

    public void writeInt8(int... ints) {
        for (int i : ints) {
            if (offset + 8 <= max) {
                data |= (long) (i & 0xFF) << offset;
                offset += 8;
            } else {
                throw new IllegalStateException("Exceeded 64-bit limit");
            }
        }
    }

    public boolean readBool() {
        if (offset < 64) {
            boolean b = (data & (1L << offset)) != 0;
            offset++;
            return b;
        } else {
            throw new IllegalArgumentException("Bit position exceeds 64-bit range");
        }
    }

    public int readInt4() {
        if (offset + 4 <= 64) {
            int dat = (int) ((data >> offset) & 0xF);
            offset+=4;
            return dat;
        } else {
            throw new IllegalArgumentException("Bit position exceeds 64-bit range");
        }
    }

    public int readInt8() {
        if (offset + 8 <= 64) {
            int dat = (int) ((data >> offset) & 0xFF);
            offset+=8;
            return dat;
        } else {
            throw new IllegalArgumentException("Bit position exceeds 64-bit range");
        }
    }

    public void reset() {
        data = 0;
        offset = 0;
    }

    public long value() {
        return data;
    }

    public int bitsUsed(){
        return offset;
    }

    public int limit(){
        return max;
    }

}
