import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

/**
 * @Classname PoiAction
 * @Description TODO
 * @Date 2021/11/2 14:28
 * @Created by 晨曦
 */
public class PoiAction extends ImapAction {
    private StringBuilder url = new StringBuilder("https://restapi.amap.com/v3/place/text");
    private String keywords;
    private String types;
    private String city;
    private Boolean citylimit;
    private String children;
    private Integer offset;
    private Integer page;

    public PoiAction(String key) {
        super(key);
    }

    public StringBuilder getUrl() {
        return url;
    }

    public void setUrl(StringBuilder url) {
        this.url = url;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getCitylimit() {
        return citylimit;
    }

    public void setCitylimit(Boolean citylimit) {
        this.citylimit = citylimit;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    Response exe() throws Exception {
        super.exe();
        if (StringUtils.isAllEmpty(keywords,types)){
            throw new Exception("关键字和分类不能都为空");
        }
        this.url.append("?key=");
        this.url.append(this.key);
        if (StringUtils.isNotEmpty(keywords)){
            this.url.append("&keywords=");
            this.url.append(keywords);
        }
        if (StringUtils.isNotEmpty(types)){
            this.url.append("&types=");
            this.url.append(types);
        }
        if (StringUtils.isNotEmpty(city)){
            this.url.append("&city=");
            this.url.append(city);
        }
        if (citylimit!=null){
            this.url.append("&citylimit=");
            this.url.append(String.valueOf(citylimit));
        }
        if (StringUtils.isNotEmpty(children)){
            this.url.append("&children=");
            this.url.append(children);
        }
        if (offset!=null&&offset<=25){
            this.url.append("&offset=");
            this.url.append(String.valueOf(offset));
        }
        if (page!=null){
            this.url.append("&page=");
            this.url.append(String.valueOf(page));
        }
        this.url.append("&extensions=all");
        this.url.append("&citylimit=true");
        final Request request = new Request.Builder()
                .url(this.url.toString()).get().build();
        Call call = super.okHttpClient.newCall(request);
        return call.execute();
    }
}
