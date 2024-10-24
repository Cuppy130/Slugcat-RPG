package engine.network;

public class HardcodeError {
    public static String get(int err){
        switch (err) {
            case 0x00:
                return "Incorrect mesage length.";
            case 0x01:
                return "Incorrect mesage format.";
            case 0x02:
                return "Cannot join twice.";
            case 0x03:
                return "Incorrect Password.";
            case 0x04:
                return "Max players on server.";
            default:
                return "";
        }
    }
}
