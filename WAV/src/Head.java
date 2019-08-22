/** 
 * wave文件头信息 
 * @author Carlos IP
 * 
 */  
public class Head {  
      
    public int riff_id;           //4 byte , 'RIFF'  
    public int file_size;         //4 byte , 文件长度（数据长度+36）  
    public int riff_type;         //4 byte , 'WAVE'  
  
    public int fmt_id;            //4 byte , 'fmt'  
    public int fmt_size;          //4 byte , 数值为16或18，18则最后又附加信息  
    public short fmt_tag;          //2 byte , 编码方式，一般为0x0001  
    public short fmt_channel;     //2 byte , 声道数目，1--单声道；2--双声道  
    public int fmt_samplesPerSec;//4 byte , 采样频率  
    public int avgBytesPerSec;   //4 byte , 每秒所需字节数,记录每秒的数据量  
    public short blockAlign;      //2 byte , 数据块对齐单位(每个采样需要的字节数)  
    public short bitsPerSample;   //2 byte , 每个采样需要的bit数  
  
    public int data_id;           //4 byte , 字符data  
    public int data_size;         //4 byte , 数据长度  
      
    public int getRiff_id() {  
        return riff_id;  
    }  
    public void setRiff_id(int riff_id) {  
        this.riff_id = riff_id;  
    }  
    public int getFile_size() {  
        return file_size;  
    }  
    public void setFile_size(int file_size) {  
        this.file_size = file_size;  
    }  
    public int getRiff_type() {  
        return riff_type;  
    }  
    public void setRiff_type(int riff_type) {  
        this.riff_type = riff_type;  
    }  
    public int getFmt_id() {  
        return fmt_id;  
    }  
    public void setFmt_id(int fmt_id) {  
        this.fmt_id = fmt_id;  
    }  
    public int getFmt_size() {  
        return fmt_size;  
    }  
    public void setFmt_size(int fmt_size) {  
        this.fmt_size = fmt_size;  
    }  
    public short getFmt_tag() {  
        return fmt_tag;  
    }  
    public void setFmt_tag(short fmt_tag) {  
        this.fmt_tag = fmt_tag;  
    }  
    public short getFmt_channel() {  
        return fmt_channel;  
    }  
    public void setFmt_channel(short fmt_channel) {  
        this.fmt_channel = fmt_channel;  
    }  
    public int getFmt_samplesPerSec() {  
        return fmt_samplesPerSec;  
    }  
    public void setFmt_samplesPerSec(int fmt_samplesPerSec) {  
        this.fmt_samplesPerSec = fmt_samplesPerSec;  
    }  
    public int getAvgBytesPerSec() {  
        return avgBytesPerSec;  
    }  
    public void setAvgBytesPerSec(int avgBytesPerSec) {  
        this.avgBytesPerSec = avgBytesPerSec;  
    }  
    public short getBlockAlign() {  
        return blockAlign;  
    }  
    public void setBlockAlign(short blockAlign) {  
        this.blockAlign = blockAlign;  
    }  
    public short getBitsPerSample() {  
        return bitsPerSample;  
    }  
    public void setBitsPerSample(short bitsPerSample) {  
        this.bitsPerSample = bitsPerSample;  
    }  
    public int getData_id() {  
        return data_id;  
    }  
    public void setData_id(int data_id) {  
        this.data_id = data_id;  
    }  
    public int getData_size() {  
        return data_size;  
    }  
    public void setData_size(int data_size) {  
        this.data_size = data_size;  
    }  
      
}
