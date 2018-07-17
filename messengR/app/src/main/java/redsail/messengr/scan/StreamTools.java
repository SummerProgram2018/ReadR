package redsail.messengr.scan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Xiangjianqiu on 2018/4/2.
 */

public class StreamTools {
    public static String readStream(InputStream is){
        try{
            //字节数组输出流在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组缓冲区中
            /*
            ByteArrayOutputStream类是在创建它的实例时，程序内部创建一个byte型别数组的缓冲区，
            然后利用ByteArrayOutputStream和ByteArrayInputStream的实例向数组中写入或读出byte型数据。
            在网络传输中我们往往要传输很多变量，我们可以利用ByteArrayOutputStream把所有的变量收集到一起，
            然后一次性把数据发送出去。
            http://www.apihome.cn/api/java/ByteArrayOutputStream.html
            此类实现了一个输出流，其中的数据被写入一个 byte 数组。缓冲区会随着数据的不断写入而自动增长。
            可使用 toByteArray() 和 toString() 获取数据。
             */
            //构造方法创建一个32字节（默认大小）的缓冲区。
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            // 声明缓冲区
            byte[]buffer=new byte[1024];
            // 定义读取默认长度
            int len=-1;
            /*因为inputStream里面的内容你不知道具体有多长，所以无法确定到底buffer需要多长1024也许未必够用，
            所以只能把buffer当做缓存，每次读进一部分，在把buffer的内容，写到足够大的内存区。
            如果你确定你的输入流中字节数小于1024，你也可以直接返回buffer
             */
            while((len=is.read(buffer))!=-1){
                // 把缓冲区中输出到内存中
                //本例子将每次读到字节数组(buffer变量)内容写到内存缓冲区中，起到保存每次内容的作用
                baos.write(buffer,0,len);
            }
            // 关闭输入流
            is.close();
            String temptext=new String(baos.toByteArray());
            if(temptext.contains("charset=gbK")){
                return new String(baos.toByteArray(),"gbK");
            }else {
                return new String(baos.toByteArray(),"utf-8");
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }
}
