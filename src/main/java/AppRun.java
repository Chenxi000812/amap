import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Classname AppRun
 * @Description TODO
 * @Date 2021/11/2 14:27
 * @Created by 晨曦
 */
public class AppRun extends JFrame{
    private static final int MIN_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;

    private static int currentProgressTotal = MIN_PROGRESS;
    private static int currentSingle = MIN_PROGRESS;
    //定义组件
    JPanel jp1,jp2,jp3,jp4,jp5,jp6,jpb;    //定义面板
    JTextField jtf1,jtf2,jtf3,jtf4,jtf5;        //定义文本框
    JLabel jlb1,jlb2,jlb3,jlb4,jlb5,jlb6;        //定义标签
    JButton jb1;        //定义按钮
    JProgressBar jProgressBar1,jProgressBar2;
    public AppRun()        //构造函数
    {
        //创建组件
        jp1=new JPanel();    //创建三个面板
        jp2=new JPanel();
        jp3=new JPanel();
        jp4=new JPanel();
        jp5=new JPanel();
        jpb=new JPanel();

        jlb1=new JLabel("KEY");    //创建两个标签
        jlb2=new JLabel("关键字");
        jlb3=new JLabel("分类代码");
        jlb4=new JLabel("城市");

        jb1=new JButton("生成excel");    //创建两个按钮

        jtf1=new JTextField(10);     //创建文本框
        jtf2=new JTextField(10);    //创建密码框
        jtf3=new JTextField(10);
        jtf4=new JTextField(10);

        jProgressBar1 = new JProgressBar();
        jProgressBar1.setMinimum(MIN_PROGRESS);
        jProgressBar1.setMaximum(MAX_PROGRESS);

        jProgressBar1.setValue(currentProgressTotal);
        jProgressBar1.setStringPainted(true);

        jProgressBar2 = new JProgressBar();
        jProgressBar2.setMinimum(MIN_PROGRESS);
        jProgressBar2.setMaximum(MAX_PROGRESS);

        jProgressBar2.setValue(currentSingle);
        jProgressBar2.setStringPainted(true);
        //设置布局管理器
        this.setLayout(new GridLayout(7,1));    //网格布局，3行一列

        //添加组件
        this.add(jp1);    //添加面板
        this.add(jp2);
        this.add(jp3);
        this.add(jp4);
        this.add(jpb);

        jp1.add(jlb1);    //添加面板1的标签和文本框
        jp1.add(jtf1);

        jp2.add(jlb2);    //添加面板2的标签和密码框
        jp2.add(jtf2);

        jp3.add(jlb3);
        jp3.add(jtf3);

        jp4.add(jlb4);
        jp4.add(jtf4);

        jb1.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    File xlsFile = new File(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xls");
                                    // 创建一个工作簿
                                    WritableWorkbook workbook = Workbook.createWorkbook(xlsFile);
                                    int index = 1;
                                    // 创建一个工作表
                                    WritableSheet sheet = workbook.createSheet("data", 0);
                                    sheet.addCell(new Label(0,0,"省"));
                                    sheet.addCell(new Label(1,0,"市"));
                                    sheet.addCell(new Label(2,0,"区"));
                                    sheet.addCell(new Label(3,0,"地址"));
                                    sheet.addCell(new Label(4,0,"名称"));
                                    sheet.addCell(new Label(5,0,"类型"));
                                    sheet.addCell(new Label(6,0,"时间"));
                                    sheet.addCell(new Label(7,0,"坐标"));
                                    List<String> citycodes = new ArrayList<>();
                                    if (StringUtils.isEmpty(jtf4.getText())){
                                        BufferedReader reader = new BufferedReader(new FileReader(new File("cityCode")));
                                        String code;
                                        while ((code = reader.readLine())!=null){
                                            citycodes.add(code);
                                        }
                                    }else citycodes.add(jtf4.getText());
                                    jProgressBar1.setMaximum(citycodes.size());
                                    for (int j=0;j<citycodes.size();j++) {
                                        jProgressBar1.setValue(j+1);
                                        int page = 0;
                                        while (true){
                                            PoiAction poiAction = new PoiAction(jtf1.getText());
                                            poiAction.setKeywords(jtf2.getText());
                                            poiAction.setTypes(jtf3.getText());
                                            poiAction.setCity(citycodes.get(j));
                                            poiAction.setOffset(25);
                                            poiAction.setPage(++page);
                                            try {
                                                Response exe = poiAction.exe();
                                                String ob = exe.body().string();
                                                JSONObject m = JSONObject.parseObject(ob);
                                                if (m.getString("status").equals("1")&&m.getString("info").equals("OK")&&!m.getString("count").equals("0")){
                                                    JSONArray pois = m.getJSONArray("pois");
                                                    for (int i = 0; i < pois.size(); i++) {
                                                        JSONObject poi = pois.getJSONObject(i);
                                                        sheet.addCell(new Label(0,index,poi.getString("pname")));
                                                        sheet.addCell(new Label(1,index,poi.getString("cityname")));
                                                        sheet.addCell(new Label(2,index,poi.getString("adname")));
                                                        sheet.addCell(new Label(3,index,poi.getString("address")));
                                                        sheet.addCell(new Label(4,index,poi.getString("name")));
                                                        sheet.addCell(new Label(5,index,poi.getString("type")));
                                                        sheet.addCell(new Label(6,index,poi.getString("timestamp")));
                                                        sheet.addCell(new Label(7,index,poi.getString("location")));
                                                        index++;
                                                    }
                                                    jProgressBar2.setMaximum(Integer.parseInt(m.getString("count")));
                                                    jProgressBar2.setValue((page+1)*25);
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    jProgressBar1.setValue(jProgressBar1.getMaximum());
                                    jProgressBar2.setValue(jProgressBar2.getMaximum());
                                    workbook.write();
                                    workbook.close();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                ).start();

            }
        });

        jpb.add(jb1);    //添加面板3的按钮
        this.add(jProgressBar1);
        this.add(jProgressBar2);
        //设置窗口属性
        this.setTitle("高德poi");    //创建界面标题
        this.setSize(300, 300);        //设置界面像素
        this.setLocation(500, 100);    //设置界面初始位置
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //设置界面和虚拟机一起关闭
        this.setVisible(true);    //设置界面可显示
    }

    public static void main(String[] args) {
        AppRun a=new AppRun();    //显示界面
    }
}
