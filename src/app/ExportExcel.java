package app;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import com.sun.prism.paint.Color;  

/**
 * 
 * @author BonizLee
 *
 * @param <T>
 */
public class ExportExcel<T> {
	 public void exportExcel(Collection<T> dataset, OutputStream out)  
	    {  
	        exportExcel("Sheet1", null, dataset, out, "yyyy-MM-dd");  
	    }  
	  
	    public void exportExcel(String[] headers, Collection<T> dataset,  
	            OutputStream out)  
	    {  
	        exportExcel("Sheet1", headers, dataset, out, "yyyy-MM-dd");  
	    }  
	  
	    public void exportExcel(String[] headers, Collection<T> dataset,  
	            OutputStream out, String pattern)  
	    {  
	        exportExcel("Sheet1", headers, dataset, out, pattern);  
	    }  
	  
	    /** 
	     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上 
	     *  
	     * @param title 
	     *            表格sheet名 
	     * @param headers 
	     *            表格属性列名数组 
	     * @param dataset 
	     *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的 
	     *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据) 
	     * @param out 
	     *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中 
	     * @param pattern 
	     *            如果有时间数据，设定输出格式。默认为"yyyy-MM-dd" 
	     */  
	    
	    public void exportExcel(String title, String[] headers,  
	            Collection<T> dataset, OutputStream out, String pattern)  
	    {  
	        // 声明一个工作薄  
	        //HSSFWorkbook workbook = new HSSFWorkbook();
	    	int rowMaxCache = 100;	    	 
	    	XSSFWorkbook xssfWb = new XSSFWorkbook();
	    	SXSSFWorkbook workbook = new SXSSFWorkbook(xssfWb, rowMaxCache);
	        // 生成一个表格  
	    	SXSSFSheet sheet = workbook.createSheet(title);  
	        // 设置表格默认列宽度为15个字节  
	        //sheet.setDefaultColumnWidth((short) 15);
	        
	        // 生成一个样式  
	        CellStyle style = workbook.createCellStyle();  
	        // 设置这些样式  	       
	       
	        //style.setFillForegroundColor(HSSFColor.WHITE.index);
	        style.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);	        
	        style.setBorderBottom(BorderStyle.THIN);
	        style.setBorderRight(BorderStyle.THIN);
	        style.setBorderLeft(BorderStyle.THIN);
	        style.setBorderTop(BorderStyle.THIN);  
	        style.setAlignment(HorizontalAlignment.CENTER);
	        
	        // 生成一个字体  
	        Font font = workbook.createFont();  
	        font.setColor(Font.COLOR_NORMAL);  
	        font.setFontHeightInPoints((short) 12);
	        font.setBold(true);
	        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	        // 把字体应用到当前的样式  
	        style.setFont(font);  
	        // 生成并设置另一个样式  
	        CellStyle style2 = workbook.createCellStyle();  	        
	        style2.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
	        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
	        style2.setBorderBottom(BorderStyle.THIN);  
	        style2.setBorderLeft(BorderStyle.THIN);  
	        style2.setBorderRight(BorderStyle.THIN);  
	        style2.setBorderTop(BorderStyle.THIN);  
	        style2.setAlignment(HorizontalAlignment.CENTER);
	        style2.setVerticalAlignment(VerticalAlignment.CENTER);
	        //style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
	        // 生成另一个字体  
	        Font font2 = workbook.createFont();  
	        //font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
	        
	        // 把字体应用到当前的样式  
	        style2.setFont(font2);  
	        
	        Font font3 = workbook.createFont();  
            font3.setColor(Font.COLOR_NORMAL);  
	  
	        // 声明一个画图的顶级管理器  
	        SXSSFDrawing patriarch = sheet.createDrawingPatriarch();  
	        // 定义注释的大小和位置,详见文档  
	        //HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));  
	        // 设置注释内容  
	        //comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));  
	        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.  
	        //comment.setAuthor("Boniz");  
	  
	        // 产生表格标题行  
	        SXSSFRow row = sheet.createRow(0);
	        int[] fieldwidth=new int[headers.length];
	        for (short i = 0; i < headers.length; i++)  
	        {  
	        	fieldwidth[i]=headers[i].getBytes().length*2*256;
	        	sheet.setColumnWidth(i, fieldwidth[i]);//根据标题内容设置列宽度
	        	
	            SXSSFCell cell = row.createCell(i);  
	            cell.setCellStyle(style);  
	            XSSFRichTextString text = new XSSFRichTextString(headers[i]);  
	            cell.setCellValue(text);  
	        }  
	  
	        // 遍历集合数据，产生数据行  
	        Iterator<T> it = dataset.iterator();  
	        int index = 0;  
	        while (it.hasNext())  
	        {  
	            index++;  
	            row = sheet.createRow(index);  
	            T t = (T) it.next();  
	            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值  
	            Field[] fields = t.getClass().getDeclaredFields();  
	            for (short i = 0; i < fields.length; i++)  
	            {  
	                SXSSFCell cell = row.createCell(i);  
	                cell.setCellStyle(style2);  
	                Field field = fields[i];  
	                String fieldName = field.getName();  
	                String getMethodName = "get"  
	                        + fieldName.substring(0, 1).toUpperCase()  
	                        + fieldName.substring(1);  
	                try  
	                {  
	                    Class tCls = t.getClass();  
	                    Method getMethod = tCls.getMethod(getMethodName,new Class[]{});  
	                    Object value = getMethod.invoke(t, new Object[]  
	                    {}); 
	                    if(value==null)
	                    	value="";
	                    // 判断值的类型后进行强制类型转换  
	                    String textValue = null;  	                    
	                    if (value instanceof Date)  
	                    {  
	                        Date date = (Date) value;  
	                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
	                        textValue = sdf.format(date);  
	                    }  
	                    else if (value instanceof byte[])  
	                    {  
	                        // 有图片时，设置行高为60px;  
	                        row.setHeightInPoints(60);  
	                        // 设置图片所在列宽度为80px,注意这里单位的一个换算  
	                        sheet.setColumnWidth(i, (short) (35.7 * 80));  
	                        // sheet.autoSizeColumn(i);  
	                        byte[] bsValue = (byte[]) value;  
	                        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0,  
	                                1023, 255, (short) 6, index, (short) 6, index);  
	                        
	                        anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
	                        patriarch.createPicture(anchor, workbook.addPicture(  
	                                bsValue, XSSFWorkbook.PICTURE_TYPE_JPEG));  
	                    }  
	                    else  
	                    {  
	                        // 其它数据类型都当作字符串简单处理  
	                        textValue = value.toString();  
	                    }  
	                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成  
	                    if (textValue != null)  
	                    {  
	                    	int fwidth=textValue.getBytes().length*2*256;
	                    	if(fwidth>fieldwidth[i]) {
	                    		fieldwidth[i]=fwidth;
	                    		sheet.setColumnWidth(i, fieldwidth[i]);//根据标题内容设置列宽度
	                    	}
	                        Pattern pDouble = Pattern.compile("^\\d+(\\.\\d+)?$");	                        
	                        Matcher matcherDouble = pDouble.matcher(textValue);  
	                        Pattern pInt = Pattern.compile("^\\d+$");	                        
	                        Matcher matcherInt = pInt.matcher(textValue); 
	                        
	                        if (matcherDouble.matches())  
	                        {  
	                            // 是数字当作double处理  
	                            cell.setCellValue(Double.parseDouble(textValue));  
	                        }  
	                        else if(matcherInt.matches()) {
	                        	cell.setCellValue(Integer.parseInt(textValue));
	                        }	                        	
	                        else  
	                        {  
	                            XSSFRichTextString richString = new XSSFRichTextString(  
	                                    textValue);  
	                            
	                            richString.applyFont(font3);  
	                            cell.setCellValue(richString);  
	                        }  
	                    }  
	                }  
	                catch (SecurityException e)  
	                {  
	                    e.printStackTrace();  
	                }  
	                catch (NoSuchMethodException e)  
	                {  
	                    e.printStackTrace();  
	                }  
	                catch (IllegalArgumentException e)  
	                {  
	                    e.printStackTrace();  
	                }  
	                catch (IllegalAccessException e)  
	                {  
	                    e.printStackTrace();  
	                }  
	                catch (InvocationTargetException e)  
	                {  
	                    e.printStackTrace();  
	                }  
	                finally  
	                {  
	                    // 清理资源	                
	                }  
	            }  
	        }  
	        try  
	        {  
	            workbook.write(out); 
	            workbook.close();
	        }  
	        catch (IOException e)  
	        {  
	            e.printStackTrace();  
	        }  
	    }  

}
