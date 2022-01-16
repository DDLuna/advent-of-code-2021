import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class Day16 {
    private static int versionSum = 0;
    private static String transmission;
    private static int index = 0;

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String transmissionHex = Files.readAllLines(Path.of("inputs/day16.txt")).get(0);
        transmission = new BigInteger("1" + transmissionHex, 16).toString(2).substring(1);
        BigInteger packetValue = parseNextPacket();
        System.out.println("Version sum: " + versionSum);
        System.out.println("Packet value: " + packetValue);
        System.out.println("Run time: " + (System.currentTimeMillis() - start));
    }

    private static BigInteger parseNextPacket() {
        versionSum += Integer.parseInt(transmission.substring(index, index + 3), 2);
        int typeId = Integer.parseInt(transmission.substring(index + 3, index + 6), 2);
        index += 6;
        if (typeId == 4) {
            return parseNextLiteral();
        } else {
            return parseNextOperator(typeId);
        }
    }

    private static BigInteger parseNextOperator(int operationType) {
        var subPacketValues = new ArrayList<BigInteger>();
        if (transmission.charAt(index) == '0') {
            int subPacketsLength = Integer.parseInt(transmission.substring(index + 1, index + 16), 2);
            index += 16;
            int targetIndex = subPacketsLength + index;
            while (index < targetIndex) {
                subPacketValues.add(parseNextPacket());
            }
        } else {
            int subPacketsCount = Integer.parseInt(transmission.substring(index + 1, index + 12), 2);
            index += 12;
            for (int i = 0; i < subPacketsCount; i++) {
                subPacketValues.add(parseNextPacket());
            }
        }
        return operateSubPackets(subPacketValues, operationType);
    }

    private static BigInteger operateSubPackets(ArrayList<BigInteger> subPacketValues, int operationType) {
        return switch (operationType) {
            case 0 -> subPacketValues.stream().reduce(BigInteger.ZERO, BigInteger::add);
            case 1 -> subPacketValues.stream().reduce(BigInteger.ONE, BigInteger::multiply);
            case 2 -> subPacketValues.stream().min(BigInteger::compareTo).orElseThrow();
            case 3 -> subPacketValues.stream().max(BigInteger::compareTo).orElseThrow();
            case 5 -> subPacketValues.get(0).compareTo(subPacketValues.get(1)) > 0 ? BigInteger.ONE : BigInteger.ZERO;
            case 6 -> subPacketValues.get(0).compareTo(subPacketValues.get(1)) < 0 ? BigInteger.ONE : BigInteger.ZERO;
            case 7 -> subPacketValues.get(0).equals(subPacketValues.get(1)) ? BigInteger.ONE : BigInteger.ZERO;
            default -> throw new RuntimeException("operationType invalid " + operationType);
        };
    }

    private static BigInteger parseNextLiteral() {
        var builder = new StringBuilder();
        while (transmission.charAt(index) == '1') {
            builder.append(transmission, index + 1, index + 5);
            index += 5;
        }
        builder.append(transmission, index + 1, index + 5);
        index += 5;
        return new BigInteger(builder.toString(), 2);
    }
}
