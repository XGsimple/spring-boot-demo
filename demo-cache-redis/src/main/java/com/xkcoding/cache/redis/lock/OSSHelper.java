package com.xkcoding.cache.redis.lock;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xugangq
 * @date 2022/1/20 20:24
 */
@Slf4j
public class OSSHelper {

    //    /**
    //     * 结束点
    //     */
    //    private static String endpoint = null;
    //    /**
    //     * 访问id
    //     */
    //    private static String accessKeyId = null;
    //    /**
    //     * 秘钥
    //     */
    //    private static String accessKeySecret = null;
    //
    //    /**
    //     * 协议
    //     */
    //    private static String protocol = null;
    //
    //    /**
    //     * 私有bucket name
    //     */
    //    private static String privateBucket = null;
    //    /**
    //     * 公开读bucket name
    //     */
    //    private static String publicBucket = null;
    //
    //    /**
    //     * 初始化标志
    //     */
    //    private static boolean init =false;
    //
    //    /**
    //     * 初始化变量
    //     */
    //    public static void init(){
    //        if(init)
    //            return;
    //        endpoint = AppConfig.getInstance().getString("OSS.endpoint","oss-cn-beijing.aliyuncs.com");
    //        accessKeyId = AppConfig.getInstance().getString("OSS.accessKeyId","CsX55AXH5vfOjdcV");
    //        accessKeySecret = AppConfig.getInstance().getString("OSS.accessKeySecret","CyhNwKBM9bbdUrWsJNLbCuiE79GiwV");
    //        privateBucket = AppConfig.getInstance().getString("OSS.privateBucket","ncc-hr-private-dev");
    //        publicBucket = AppConfig.getInstance().getString("OSS.publicBucket","ncc-hr-pub-dev");
    //        protocol = AppConfig.getInstance().getString("OSS.protocol","https");
    //        init = true;
    //    }
    //
    //    /**
    //     * 取得OSS客户端对象
    //     * @return OSS客户端对象
    //     */
    //    public static OSSClient getClient(){
    //        init();
    //        ClientConfiguration config = new ClientConfiguration();
    //        config.setProtocol(Protocol.valueOf(protocol.toUpperCase()));
    //        return new OSSClient(endpoint, accessKeyId, accessKeySecret,config);
    //    }
    //
    //    /**
    //     * 创建文件过期删除的的bucket
    //     * @param bucketName　bucket名
    //     * @param days　超期时间（天）
    //     */
    //    public static void createTempBucket(String bucketName,int days){
    //        OSSClient client = getClient();
    //        try{
    //            client.createBucket(bucketName);
    //            SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);
    //            request.AddLifecycleRule(new LifecycleRule(bucketName+"expire", null, LifecycleRule.RuleStatus.Enabled, days));
    //            client.setBucketLifecycle(request);
    //        } catch (OSSException oe) {
    //            handleOSSException(oe);
    //        } catch (ClientException ce) {
    //            handleClientException(ce);
    //        }finally {
    //            client.shutdown();
    //        }
    //    }
    //
    //    /**
    //     * 删除bucket,需要有权限
    //     * @param bucketName　bucket名称
    //     */
    //    public static void deleteBucket(String bucketName){
    //        OSSClient client = getClient();
    //        try{
    //            client.deleteBucket(bucketName);
    //        } catch (OSSException oe) {
    //            handleOSSException(oe);
    //        } catch (ClientException ce) {
    //            handleClientException(ce);
    //        }finally {
    //            client.shutdown();
    //        }
    //
    //    }
    //
    //
    //    /**
    //     * 创建私有库里的目录
    //     * @param folder　目录名
    //     */
    //    public static void createFolderInPrivate(String folder){
    //        init();
    //        createFolder(privateBucket,folder);
    //    }
    //
    //    /**
    //     * 创建公有读中的目录
    //     * @param folder　目录名
    //     */
    //    public static void createFolderInPub(String folder){
    //        init();
    //        createFolder(publicBucket,folder);
    //    }
    //
    //    /**
    //     * 在指定的bucket中创建目录
    //     * @param bucketName bucket　名
    //     * @param folder　目录名
    //     */
    //    public static void createFolder(String bucketName, String folder){
    //
    //        if(!bucketName.endsWith("/")){
    //            bucketName+="/";
    //        }
    //        OSSClient client = getClient();
    //        try{
    //            client.putObject(bucketName, folder, new ByteArrayInputStream(new byte[0]));
    //        } catch (OSSException oe) {
    //            handleOSSException(oe);
    //        } catch (ClientException ce) {
    //            handleClientException(ce);
    //        }finally {
    //            client.shutdown();
    //        }
    //
    //    }
    //
    //    /**
    //     * 取得私有库中指定附件名称的url,有超时限制
    //     * @param key　附件名
    //     * @return url串
    //     */
    //    public static String getUrlInPrivate(String key){
    //        init();
    //        return getExpireUrl(privateBucket,key,30);
    //    }
    //
    //    /**
    //     * 取得公有读库中指定附件名称的url
    //     * @param key　附件名称
    //     * @return url串
    //     */
    //    public static String getUrlPub(String key){
    //        init();
    //        return getUrl(publicBucket,key);
    //    }
    //
    //    /**
    //     * 取得指定公有读类型bucket中的指定附件名称的url
    //     * @param bucketName　公有读类型bucket名称
    //     * @param key　附件名
    //     * @return url串
    //     */
    //    public static String getUrl(String bucketName, String key){
    //        try {
    //            int i = key.lastIndexOf("/");
    //            if(i < 0){
    //                key = URLEncoder.encode(key, "UTF-8");
    //            }else{
    //                key = key.substring(0,i+1)+URLEncoder.encode(key.substring(i+1),"UTF-8");
    //            }
    //            return protocol+"://"+bucketName+"."+endpoint+ "/" + key;
    //        } catch (UnsupportedEncodingException e) {
    //            log.error("Error Code:       " + e);
    //            throw new AdderRuntimeException(e);
    //        }
    //    }
    //
    //    /**
    //     * 取得指定私有类型bucket中指定附件名称的url
    //     * @param bucketName　私有类型bucket名称
    //     * @param key　附件名
    //     * @param expirationMins　超时时长
    //     * @return url串
    //     */
    //    public static String getExpireUrl(String bucketName, String key, int expirationMins){
    //
    //        // 设置URL过期时间为1小时
    //        Date expiration = new Date(System.currentTimeMillis() + 1000L * 60 * expirationMins);
    //        OSSClient client = getClient();
    //        // 生成URL
    //        try{
    //            String url = client.generatePresignedUrl(bucketName, key, expiration).toString();
    //            if(!url.startsWith(protocol +"://")){
    //                url = protocol + "://" +url;
    //            }
    //            return url;
    //        } catch (OSSException oe) {
    //            handleOSSException(oe);
    //        } catch (ClientException ce) {
    //            handleClientException(ce);
    //        }finally {
    //            client.shutdown();
    //        }
    //        return null;
    //    }
    //
    //    /**
    //     * 删除指定私有库中的附件
    //     * @param key　附件名
    //     */
    //    public static void deleteInPrivate(String key){
    //        init();
    //        deleteFile(privateBucket,key);
    //    }
    //
    //    /**
    //     * 删除公有库中的附件
    //     * @param key　附件名
    //     */
    //    public static void deleteInPub(String key){
    //        init();
    //        deleteFile(publicBucket,key);
    //    }
    //
    //    /**
    //     * 删除指定bucket中的指定附件
    //     * @param bucketName　bucket名称
    //     * @param key　附件名
    //     */
    //    public static void deleteFile(String bucketName, String key){
    //        OSSClient client = getClient();
    //        // 生成URL
    //        try{
    //            // 删除Object
    //            client.deleteObject(bucketName, key);
    //        } catch (OSSException oe) {
    //            handleOSSException(oe);
    //        } catch (ClientException ce) {
    //            handleClientException(ce);
    //        }finally {
    //            client.shutdown();
    //        }
    //
    //    }
    //
    //
    //    /**
    //     * 上传附件到私有库中
    //     * @param key　附件名
    //     * @param is　附件输入流
    //     */
    //    public static String uploadInPrivate(String key, InputStream is){
    //        init();
    //        return uploadFile(privateBucket,key, is);
    //    }
    //
    //    /**
    //     * 上传附件到公有库中
    //     * @param key 附件名
    //     * @param is　附件输入流
    //     */
    //    public static String uploadInPub(String key,InputStream is){
    //        init();
    //        return uploadFile(publicBucket,key, is);
    //    }
    //
    //    /**
    //     * 上传附件到指定bucket中
    //     * @param bucketName　bucket名
    //     * @param key　附件名
    //     * @param is　附件输入流
    //     */
    //    public static String uploadFile(String bucketName, String key, InputStream is){
    //        OSSClient client = getClient();
    //        // 生成URL
    //        try{
    //            // 上传文件
    //            client.putObject(bucketName, key,is);
    //        } catch (OSSException oe) {
    //            handleOSSException(oe);
    //        } catch (ClientException ce) {
    //            handleClientException(ce);
    //        }finally {
    //            client.shutdown();
    //        }
    //        return key;
    //    }
    //
    //    /**
    //     * 从私有仓库中下载附件
    //     * @param key　附件名
    //     * @param fileRoot　文件存放的全路径
    //     */
    //    public static File downloadInPrivate(String key, String fileRoot){
    //        init();
    //        return downloadFile(privateBucket,key, fileRoot);
    //    }
    //
    //    /**
    //     * 从私有仓库中下载附件
    //     * @param key　附件名
    //     * @param fileRoot　文件存放的全路径
    //     */
    //    public static File downloadInPub(String key, String fileRoot){
    //        init();
    //        return downloadFile(publicBucket,key, fileRoot);
    //    }
    //
    //    /**
    //     * 从指定bucket中下载附件
    //     * @param bucketName　bucket名
    //     * @param key　附件名
    //     * @param fileRoot　文件存放的全路径
    //     */
    //    public static File downloadFile(String bucketName, String key, String fileRoot){
    //        OSSClient client = getClient();
    //        try{
    //            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
    //            File file = new File(fileRoot);
    //            if(!file.exists()){
    //                file.createNewFile();
    //            }
    //            client.getObject(getObjectRequest, file);
    //            return file;
    //        } catch (OSSException oe) {
    //            handleOSSException(oe);
    //        } catch (ClientException ce) {
    //            handleClientException(ce);
    //        } catch (IOException e) {
    //            throw new AdderRuntimeException(e);
    //        }finally {
    //            client.shutdown();
    //        }
    //        return null;
    //    }
    //
    //    /**
    //     * 从私有仓库中下载附件
    //     * @param key　附件名
    //     * @return 附件输入流
    //     */
    //    public static InputStream downloadInPrivate(String key){
    //        init();
    //        return downloadFile(privateBucket,key);
    //    }
    //
    //    /**
    //     * 从私有仓库中下载附件
    //     * @param key　附件名
    //     * @return 附件输入流
    //     */
    //    public static InputStream downloadInPub(String key){
    //        init();
    //        return downloadFile(publicBucket,key);
    //    }
    //
    //    /**
    //     * 从指定bucket中下载附件
    //     * @param bucketName　bucket名
    //     * @param key　附件名
    //     * @return 附件输入流
    //     */
    //    public static InputStream downloadFile(String bucketName, String key){
    //        ByteArrayOutputStream o = new ByteArrayOutputStream();
    //        downloadFile(bucketName,key, o);
    //        return new ByteArrayInputStream(o.toByteArray());
    //    }
    //
    //    /**
    //     * 从私有仓库中下载附件
    //     * @param key　附件名
    //     * @param os  输出流
    //     */
    //    public static void downloadInPrivate(String key, OutputStream os){
    //        init();
    //        downloadFile(privateBucket,key, os);
    //    }
    //
    //    /**
    //     * 从私有仓库中下载附件
    //     * @param key　附件名
    //     * @param os 输出流
    //     */
    //    public static void downloadInPub(String key, OutputStream os){
    //        init();
    //        downloadFile(publicBucket,key,os);
    //    }
    //    /**
    //     * 从指定bucket中下载附件
    //     * @param bucketName　bucket名
    //     * @param key　附件名
    //     * @param os 输出流
    //     */
    //    public static void downloadFile(String bucketName, String key, OutputStream os){
    //        OSSClient client = getClient();
    //        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
    //        try(InputStream is = client.getObject(getObjectRequest).getObjectContent()){
    //            IOUtils.copy(is, os);
    //        } catch (OSSException oe) {
    //            handleOSSException(oe);
    //        } catch (ClientException ce) {
    //            handleClientException(ce);
    //        } catch (IOException e) {
    //            log.error(e.getMessage(), e);
    //        } finally {
    //            client.shutdown();
    //        }
    //    }
    //
    //    /**
    //     * 判断公共桶中文件是存在
    //     * @param key 文件名
    //     * @return 存在返回true
    //     */
    //    public static boolean isExistInPub(String key){
    //
    //        init();
    //        return isExist(publicBucket,key);
    //    }
    //
    //    /**
    //     * 判断私有桶中文件是存在
    //     * @param key 文件名
    //     * @return 存在返回true
    //     */
    //    public static boolean isExistInPrivate(String key){
    //
    //        init();
    //        return isExist(privateBucket,key);
    //    }
    //
    //    /**
    //     * 判断制定桶中文件是存在
    //     * @param bucketName 桶名称
    //     * @param key 文件命名
    //     * @return 存在返回true
    //     */
    //    public static boolean isExist(String bucketName, String key){
    //
    //        OSSClient client = getClient();
    //        try{
    //            return  client.doesObjectExist(bucketName,key);
    //        } catch (OSSException oe) {
    //            handleOSSException(oe);
    //        } catch (ClientException ce) {
    //            handleClientException(ce);
    //        }finally {
    //            client.shutdown();
    //        }
    //        return false;
    //    }
    //    /**
    //     * 拷贝文件对象
    //     * @param fromKey 源键值
    //     * @param targetKey 目标键值
    //     * @return 结果路径
    //     */
    //    public static String copyInPub(String fromKey, String targetKey) {
    //        return copy(publicBucket,fromKey,publicBucket,targetKey);
    //    }
    //
    //    /**
    //     * 拷贝文件对象
    //     * @param fromKey 源键值
    //     * @param targetKey 目标键值
    //     * @return 结果路径
    //     */
    //    public static String copyInPrivate(String fromKey, String targetKey){
    //        return copy(privateBucket,fromKey,privateBucket,targetKey);
    //    }
    //
    //    /**
    //     * 拷贝文件对象
    //     * @param fromBucketName   源bucket
    //     * @param fromKey 源键值
    //     * @param targetBucketName 目标bucket
    //     * @param targetKey 目标键值
    //     * @return 结果路径
    //     */
    //    public static String copy(String fromBucketName, String fromKey,String targetBucketName, String targetKey){
    //        OSSClient client = getClient();
    //        try{
    //            CopyObjectResult r = client.copyObject(fromBucketName,fromKey,targetBucketName,targetKey);
    //            return getUrl(targetBucketName,targetKey);
    //        } catch (OSSException oe) {
    //            handleOSSException(oe);
    //        } catch (ClientException ce) {
    //            handleClientException(ce);
    //        } finally {
    //            client.shutdown();
    //        }
    //        return null;
    //    }
    //
    //    /**
    //     *　处理OSSException异常
    //     * @param oe　异常
    //     */
    //    private static void handleOSSException(OSSException oe){
    //        log.error("Caught an OSSException, which means your request made it to OSS, "
    //                  + "but was rejected with an error response for some reason.");
    //        log.error("Error Message: " + oe.getErrorMessage());
    //        log.error("Error Code:       " + oe.getErrorCode());
    //        log.error("Request ID:      " + oe.getRequestId());
    //        log.error("Host ID:           " + oe.getHostId());
    //        throw new AdderRuntimeException(oe);
    //    }
    //
    //    /**
    //     * 处理ClientException异常
    //     * @param ce　oss客户端异常
    //     */
    //    private static void handleClientException(ClientException ce){
    //        log.error("Caught an ClientException, which means the client encountered "
    //                  + "a serious internal problem while trying to communicate with OSS, "
    //                  + "such as not being able to access the network.");
    //        log.error("Error Message: " + ce.getMessage());
    //        log.error("Error Code:       " + ce.getErrorCode());
    //        throw new AdderRuntimeException(ce);
    //    }
    //
    //    public static void main(String[] args) throws FileNotFoundException {
    //        File file = new File("/home/ybo/u8c_staffmanage_2.5.sql");
    //        OSSHelper.uploadInPrivate("preset_yonsuitepre_file/u8c_staffmanage_2.5.sql", new FileInputStream(file));
    //    }
}
