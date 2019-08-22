import java.io.IOException;
import java.io.RandomAccessFile;

public class HeadSize {
	
	public static int getHeadSize(RandomAccessFile srcFis) throws IOException {
        int offset = 0;
        //riff
        getChunkId(srcFis);
        offset += 4;
        //length
        getChunkSize(srcFis);
        offset += 4;
        //wave
        getChunkId(srcFis);
        offset += 4;
        //fmt
        getChunkId(srcFis);
        offset += 4;
        //fmt length
        int skipLength = getChunkSize(srcFis);
        offset += 4;
        byte[] skipBytes = new byte[skipLength];
        srcFis.read(skipBytes);
        offset += skipLength;
        String chunkId = getChunkId(srcFis);
        offset += 4;
        while (!chunkId.equals("data")) {
            skipLength = getChunkSize(srcFis);
            offset += 4;
            skipBytes = new byte[skipLength];
            srcFis.read(skipBytes);
            offset += skipLength;
            chunkId = getChunkId(srcFis);
            offset += 4;
        }
        offset += 4;
        System.out.println("headSize="+offset);
        return offset;
    }

    private static int getChunkSize(RandomAccessFile srcFis) throws IOException {
        byte[] formatSize = new byte[4];
        srcFis.read(formatSize);
        int fisrt8 = formatSize[0] & 0xFF;  
        int fisrt16 = formatSize[1] & 0xFF;
        int fisrt24 = formatSize[2] & 0xFF;
        int fisrt32 = formatSize[3] & 0xFF;
        int chunkSize = fisrt8 | (fisrt16 << 8) | (fisrt24 << 16) | (fisrt32 << 24);
        System.out.println("ChunkSize=" + chunkSize);
        return chunkSize;
    }

    private static String getChunkId(RandomAccessFile srcFis) throws IOException {
        byte[] bytes = new byte[4];
        srcFis.read(bytes);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append((char) bytes[i]);
        }
        String chunkId = stringBuilder.toString();
        System.out.println("ChunkId=" + chunkId);
        return chunkId;
    }
    
    public static void main(String[] args) throws IOException {
    	RandomAccessFile src = new RandomAccessFile("D:\\WAV\\AudioFile\\1.wav", "r");
		getHeadSize(src);
	}
}
