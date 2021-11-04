import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

/**
 * @Classname ImapAction
 * @Description TODO
 * @Date 2021/11/2 14:28
 * @Created by 晨曦
 */
public abstract class ImapAction {
    protected String key;
    protected OkHttpClient okHttpClient;

    public ImapAction(String key) {
        this.key = key;
        okHttpClient = new OkHttpClient.Builder().build();
    }
    Response exe() throws Exception {
        if (StringUtils.isEmpty(key))
            throw new Exception("缺失开发者密钥");
        return null;
    }
}
