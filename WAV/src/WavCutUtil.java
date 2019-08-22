
import it.sauronsoftware.jave.Encoder;  
import it.sauronsoftware.jave.MultimediaInfo;  
  
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileOutputStream;  
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

  
/** 
 * wav音频文件截取工具 
 * （适用于头文件长度不一样的wav音频文件，WAV音频文件的头部信息占用长度44字节(一般标准，没有拓展块的情况)） 
 * 截取一个30秒
 * @author Carlos IP
 * 
 */  
public class WavCutUtil {  
      
    /** 
     * 截取wav音频文件 
     * @param sourcepath  源文件地址 
     * @param targetpath  目标文件地址 
     * @param start  截取开始时间（秒） 
     * @param end  截取结束时间（秒） 
     * @param headSize 头文件大小 
     * return  截取成功返回true，否则返回false 
     */  
    private static boolean cut(String sourcefile, String targetfile, int start, int end, int headSize) {  
    	try{  
            if(!sourcefile.toLowerCase().endsWith(".wav") || !targetfile.toLowerCase().endsWith(".wav")){  
                return false;  
            }  
            File wav = new File(sourcefile);  
            if(!wav.exists()){  
                return false;  
            }  
            long t1 = getTimeLen(wav);  //总时长(秒)  
            if(start<0 || end<=0 || start>=t1 || end>t1 || start>=end){  
                return false;  
            }  
            FileInputStream fis = new FileInputStream(wav);  
            //long wavSize = wav.length()-44;  //音频数据大小（44为128kbps比特率wav文件头长度）  
			long wavSize = wav.length()-headSize;  //音频数据大小（wav文件头长度不一定是44）
            long splitSize = (wavSize/t1)*(end-start);  //截取的音频数据大小  
            long skipSize = (wavSize/t1)*start;  //截取时跳过的音频数据大小  
            int splitSizeInt = Integer.parseInt(String.valueOf(splitSize));  
            int skipSizeInt = Integer.parseInt(String.valueOf(skipSize));  
              
            ByteBuffer buf1 = ByteBuffer.allocate(4);  //存放文件大小,4代表一个int占用字节数  
            buf1.putInt(splitSizeInt+36);  //放入文件长度信息  
            byte[] flen = buf1.array();  //代表文件长度  
            ByteBuffer buf2 = ByteBuffer.allocate(4);  //存放音频数据大小，4代表一个int占用字节数  
            buf2.putInt(splitSizeInt);  //放入数据长度信息  
            byte[] dlen = buf2.array();  //代表数据长度  
            flen = reverse(flen);  //数组反转  
            dlen = reverse(dlen);  
            //byte[] head = new byte[44];  //定义wav头部信息数组  
			byte[] head = new byte[headSize];
            fis.read(head, 0, head.length);  //读取源wav文件头部信息  
            for(int i=0; i<4; i++){  //4代表一个int占用字节数  
                head[i+4] = flen[i];  //替换原头部信息里的文件长度  
                //head[i+40] = dlen[i];  //替换原头部信息里的数据长度  
                head[i+headSize-4] = dlen[i];  //替换原头部信息里的数据长度  
            }  
            byte[] fbyte = new byte[splitSizeInt+head.length];  //存放截取的音频数据  
            for(int i=0; i<head.length; i++){  //放入修改后的头部信息  
                fbyte[i] = head[i];  
            }  
            byte[] skipBytes = new byte[skipSizeInt];  //存放截取时跳过的音频数据  
            fis.read(skipBytes, 0, skipBytes.length);  //跳过不需要截取的数据  
            fis.read(fbyte, head.length, fbyte.length-head.length);  //读取要截取的数据到目标数组  
            fis.close();  
              
            File target = new File(targetfile);  
            if(target.exists()){  //如果目标文件已存在，则删除目标文件  
                target.delete();  
            }  
            FileOutputStream fos = new FileOutputStream(target);  
            fos.write(fbyte);  
            fos.flush();  
            fos.close();  
        }catch(IOException e){  
            e.printStackTrace();  
            return false;  
        }  
        return true;  
    }  
      
    /** 
     * 获取音频文件总时长 
     * @param filePath  文件路径 
     * @return 
     */  
    private static long getTimeLen(File file){  
        long tlen = 0;  
        if(file!=null && file.exists()){  
            Encoder encoder = new Encoder();  
            try {  
                 MultimediaInfo m = encoder.getInfo(file);  
                 long ls = m.getDuration();  
                 tlen = ls/1000;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return tlen;  
    }  
      
    /** 
    * 数组反转 
    * @param array 
    */  
    private static byte[] reverse(byte[] array){  
        byte temp;  
        int len=array.length;  
        for(int i=0;i<len/2;i++){  
            temp=array[i];  
            array[i]=array[len-1-i];  
            array[len-1-i]=temp;  
        }  
        return array;  
    }  
      
    
    //HeadSize得到头文件大小
	private static int getHeadSize(RandomAccessFile srcFis) throws IOException {
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
    
    /*
     * 把一个音频截取成多个小音频，一个30秒
     * @param 需要截取音频的文件地址
     * @param 输出截取后的音频的地址
     * 
     */
    public Boolean wavCut(String srcIn,String srcOut) throws IOException {
    	int i;
    	int c=0;
    	int d=30;
    	File file = new File(srcIn);
        long timelong = getTimeLen(file);
        if(timelong>30) {
        	int a = (int) (timelong/30);
        	int b = (int) (timelong%30);
        	if(b>0) a=a+1;
        	RandomAccessFile src = new RandomAccessFile(srcIn, "r");
        	int headSize = getHeadSize(src);
        	for(i=1;i<=a-1;i++,c=c+30,d=d+30) {
        		System.out.println(cut(srcIn,srcOut+File.separator+"Cut-"+i+".wav",c,d,headSize));  
        	}
        	if(b>0)
        	System.out.println(cut(srcIn,srcOut+File.separator+"Cut-"+i+".wav",c,c+b,headSize));  
        	
        }
        else {
        	System.out.println("少于等于30秒，不割！");
        	return false;
        }
    	return true;
    }
    
    public static void main(String[] args) throws IOException{
    	String srcIn = "D:\\WAV\\3.wav";
    	String srcOut = "D:\\WAV\\3-cut_0_";
    	RandomAccessFile src = new RandomAccessFile("D:\\WAV\\3.wav", "r");
    	int headSize = getHeadSize(src);
        System.out.println(cut(srcIn,srcOut+"10.wav",0,10,headSize));  
        System.out.println(cut(srcIn,srcOut+"20.wav",10,20,headSize));  
        System.out.println(cut(srcIn,srcOut+"30.wav",20,28,headSize));  
    }  
}
