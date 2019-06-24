package com.yiko.common.utils.poi.word;

import com.alibaba.fastjson.JSON;
import com.yiko.common.utils.poi.JsonUtil;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.w3c.dom.Node;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 * 使用POI,进行Word相关的操作
 *
 * @author xuyu
 * <p>
 * <p>
 * Modification History:
 * </p>
 * <p>
 * Date Author Description
 * </p>
 * <p>
 * ------------------------------------------------------------------
 * </p>
 * <p>
 * </p>
 * <p>
 * </p>
 */
public class MSWordTool {

    /**
     * 内部使用的文档对象
     **/
    private XWPFDocument document;

    private BookMarks bookMarks = null;

    /**
     * 为文档设置模板
     *
     * @param templatePath 模板文件名称
     */
    public void setTemplate(String templatePath) {
        try {
            this.document = new XWPFDocument(
                    POIXMLDocument.openPackage(templatePath));

            bookMarks = new BookMarks(document);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 进行标签替换的例子,传入的Map中，key表示标签名称，value是替换的信息
     *
     * @param indicator
     * @throws IOException
     * @throws FileNotFoundException
     * @throws InvalidFormatException
     */
    public void replaceBookMark(Map<String, Object> indicator)
            throws InvalidFormatException, FileNotFoundException, IOException {
        // 循环进行替换
        Iterator<String> bookMarkIter = bookMarks.getNameIterator();
        int i = 1;
        while (bookMarkIter.hasNext()) {
            try{
            String bookMarkName = bookMarkIter.next();

            // 得到标签名称
            BookMark bookMark = bookMarks.getBookmark(bookMarkName);

            // 进行替换
            if (indicator.get(bookMarkName) != null) {

                if (indicator.get(bookMarkName) instanceof com.alibaba.fastjson.JSONArray) {
                    com.alibaba.fastjson.JSONArray jsonArray = (com.alibaba.fastjson.JSONArray) indicator.get(bookMarkName);
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
                        String data = (String) iterator.next();
                        stringBuffer.append(data);
                        if (iterator.hasNext()) {
                            stringBuffer.append("， ");
                        }
                    }
                    indicator.put(bookMarkName, stringBuffer.toString());
                }
                bookMark.insertTextAtBookMark(indicator.get(bookMarkName),
                        BookMark.REPLACE,i);
            }
            }catch (Exception e){

            }
            i++;
        }
    }

    public void fillTableAtBookMark(String bookMarkName,
                                    List<Map<String, String>> content) {
//		System.out.println(content.size());
        // rowNum来比较标签在表格的哪一行
        int rowNum = 0;

        // 首先得到标签
        BookMark bookMark = bookMarks.getBookmark(bookMarkName);
        Map<String, String> columnMap = new HashMap<String, String>();
        Map<String, Node> styleNode = new HashMap<String, Node>();

        // 标签是否处于表格内
        if (bookMark.isInTable()) {

            // 获得标签对应的Table对象和Row对象
            XWPFTable table = bookMark.getContainerTable();
            XWPFTableRow row = bookMark.getContainerTableRow();
            CTRow ctRow = row.getCtRow();
            List<XWPFTableCell> rowCell = row.getTableCells();
            for (int i = 0; i < rowCell.size(); i++) {
//				System.out.println(rowCell.get(i).getText().trim());
                columnMap.put(i + "", rowCell.get(i).getText().trim());
//				 System.out.println(rowCell.get(i).getParagraphs().get(0).createRun().getFontSize());
//				 System.out.println(rowCell.get(i).getParagraphs().get(0).getCTP());
//				 System.out.println(rowCell.get(i).getParagraphs().get(0).getStyle());

                // 获取该单元格段落的xml，得到根节点
                Node node1 = rowCell.get(i).getParagraphs().get(0).getCTP()
                        .getDomNode();

                // 遍历根节点的所有子节点
                for (int x = 0; x < node1.getChildNodes().getLength(); x++) {
                    if (node1.getChildNodes().item(x).getNodeName()
                            .equals(BookMark.RUN_NODE_NAME)) {
                        Node node2 = node1.getChildNodes().item(x);

                        // 遍历所有节点为"w:r"的所有自己点，找到节点名为"w:rPr"的节点
                        for (int y = 0; y < node2.getChildNodes().getLength(); y++) {
                            if (node2.getChildNodes().item(y).getNodeName()
                                    .endsWith(BookMark.STYLE_NODE_NAME)) {

                                // 将节点为"w:rPr"的节点(字体格式)存到HashMap中
                                styleNode.put(i + "", node2.getChildNodes()
                                        .item(y));
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }

            // 循环对比，找到该行所处的位置，删除改行
            for (int i = 0; i < table.getNumberOfRows(); i++) {
                if (table.getRow(i).equals(row)) {
                    rowNum = i;
                    break;
                }
            }
            table.removeRow(rowNum);

            for (int i = 0; i < content.size(); i++) {
                // 创建新的一行,单元格数是表的第一行的单元格数,
                // 后面添加数据时，要判断单元格数是否一致
                XWPFTableRow tableRow = table.createRow();
                CTTrPr trPr = tableRow.getCtRow().addNewTrPr();
                CTHeight ht = trPr.addNewTrHeight();
                ht.setVal(BigInteger.valueOf(360));
            }

            // 得到表格行数
            int rcount = table.getNumberOfRows();
            System.out.println("rcount--" + rcount);
            for (int i = rowNum; i < rcount; i++) {

                //判断是否有数据填入新的一行中,如果没有就结束该表格的数据的填充
                if ((i - rowNum + 1) > content.size()) {
                    break;
                }

                XWPFTableRow newRow = table.getRow(i);

                // 判断newRow的单元格数是不是该书签所在行的单元格数
                if (newRow.getTableCells().size() != rowCell.size()) {

                    // 计算newRow和书签所在行单元格数差的绝对值
                    // 如果newRow的单元格数多于书签所在行的单元格数，不能通过此方法来处理，可以通过表格中文本的替换来完成
                    // 如果newRow的单元格数少于书签所在行的单元格数，要将少的单元格补上
                    int sub = Math.abs(newRow.getTableCells().size()
                            - rowCell.size());
                    System.out.println("sub---" + sub);
                    // 将缺少的单元格补上
                    /*for (int j = 0; j < sub; j++) {
                        newRow.addNewTableCell();
					}*/
                }

                List<XWPFTableCell> cells = newRow.getTableCells();

                for (int j = 0; j < cells.size(); j++) {

                    XWPFParagraph para = cells.get(j).getParagraphs().get(0);
                    XWPFRun run = para.createRun();

                    if (content.get(i - rowNum).get(columnMap.get(j + "")) != null) {

						/*System.out.println(
                                (content.get(i - rowNum).get(
								columnMap.get(j + ""))
								+ ""));*/
                        // 改变单元格的值，标题栏不用改变单元格的值
                        String a = content.get(i - rowNum).get(
                                columnMap.get(j + ""));
                        run.setText(a + "");

                        // 将单元格段落的字体格式设为原来单元格的字体格式
                        run.getCTR()
                                .getDomNode()
                                .insertBefore(
                                        styleNode.get(j + "").cloneNode(true),
                                        run.getCTR().getDomNode()
                                                .getFirstChild());
                    }

                    para.setAlignment(ParagraphAlignment.CENTER);
                }
            }
        }
    }

//    public void replaceText(Map<String, String> bookmarkMap, String bookMarkName) {
//
//        // 首先得到标签
//        BookMark bookMark = bookMarks.getBookmark(bookMarkName);
//        // 获得书签标记的表格
//        XWPFTable table = bookMark.getContainerTable();
//        // 获得所有的表
//        // Iterator<XWPFTable> it = document.getTablesIterator();
//
//        if (table != null) {
//            // 得到该表的所有行
//            int rcount = table.getNumberOfRows();
//            for (int i = 0; i < rcount; i++) {
//                XWPFTableRow row = table.getRow(i);
//
//                // 获到改行的所有单元格
//                List<XWPFTableCell> cells = row.getTableCells();
//                for (XWPFTableCell c : cells) {
//                    for (Entry<String, String> e : bookmarkMap.entrySet()) {
//                        if (c.getText().equals(e.getKey())) {
//
//                            // 删掉单元格内容
//                            c.removeParagraph(0);
//
//                            // 给单元格赋值
//                            c.setText(e.getValue());
//                        }
//                    }
//                }
//            }
//        }
//    }

    /**
     * 表格内容的明文替换
     *
     * @param bookmarkMap 明文替换内容
     * @param filePath    文档路径
     */

    public void replaceText(Map<String, String> bookmarkMap, String filePath) {

        try {



            //         String filePath = "C:\\wechatfile\\fileupload\\oEyt000uPffVya2HvQVqTUEX4_OQ\\154656674477175469144\\ZZ5129、ZZ5130《佛山市居住证业务申办表》 - 副本1546876288279331454_ce287acd-6d48-4442-812e-bafb3b2d82bc.docx";

            InputStream is = new FileInputStream(filePath);

            XWPFDocument doc = new XWPFDocument(is);

            is.close();

          /*  Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
            while (itPara.hasNext()) {
                XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                Set<String> set = bookmarkMap.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    List<XWPFRun> run=paragraph.getRuns();
                    for(int i=0;i<run.size();i++)
                    {
                        if(run.get(i).getText(run.get(i).getTextPosition())!=null &&
                                run.get(i).getText(run.get(i).getTextPosition()).contains(key))
                        {
                            *//**
                             * 参数0表示生成的文字是要从哪一个地方开始放置,设置文字从位置0开始
                             * 就可以把原来的文字全部替换掉了
                             *//*
                            String text = run.get(i).getText(run.get(i).getTextPosition());
                            text = text.replaceAll(key,bookmarkMap.get(key));
                            run.get(i).setText(text,0);
                        }
                    }
                }
            }*/

            //获取文档中所有的表格
            List<XWPFTable> tables = doc.getTables();
            List<XWPFTableRow> rows;
            List<XWPFTableCell> cells;
            for (XWPFTable table : tables) {
                //表格属性
                //获取表格对应的行
                rows = table.getRows();

                for (XWPFTableRow row : rows) {

                    //获取行对应的单元格

                    cells = row.getTableCells();

                    for (XWPFTableCell cell : cells) {

                        for (Map.Entry<String, String> e : bookmarkMap.entrySet()) {
                            try {

                                //确保在单元格内任意位置插入不修改其他内容

                                if (cell.getText().indexOf(e.getKey()) != -1) {

                                    String text = cell.getText().substring(cell.getText().lastIndexOf("_") + 1, cell.getText().length());

                                    String text2 = e.getKey().substring(e.getKey().lastIndexOf("_") + 1, e.getKey().length());

                                    int x = Integer.parseInt(text);

                                    int y = Integer.parseInt(text2);

                                    if (x == y) {

                                        String suf = cell.getText().substring(0, cell.getText().indexOf(e.getKey()));

                                        String prf = cell.getText().substring(cell.getText().indexOf(e.getKey()) + (e.getKey().length()));

                                        cell.removeParagraph(0);

                                        cell.setText(suf + e.getValue() + prf);

                                    }

                                }

                            } catch (Exception ex) {

                                if (cell.getText().indexOf(e.getKey()) != -1) {

                                    String suf = cell.getText().substring(0, cell.getText().indexOf(e.getKey()));

                                    String prf = cell.getText().substring(cell.getText().indexOf(e.getKey()) + (e.getKey().length()));

                                    cell.removeParagraph(0);

                                    cell.setText(suf + e.getValue() + prf);

                                }

                            }

                        }

                    }

                }
            }

            FileOutputStream out = new FileOutputStream(new File(filePath));

            doc.write(out);

            out.close();
        } catch (Exception e) {

        }

    }


    public void saveAs(String path) {
        File newFile = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(newFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            this.document.write(fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 根据office书签定位替换内容，能够替换图片
     *
     * @param jsonStr  json字符串内容格式 ，支持多层{"key":"value","key1":{"key11":"value11"}}
     * @param temppath 模板路径
     * @param savepath 保存路径
     * @return
     */
    public String ChangerForBookmark(MSWordTool changer, String jsonStr) {


        //Map<String, Object> content = new HashMap<String, Object>();

        try {
            // content = InterfaceUtil.parseJSON2Map(jsonStr);

            //bymao
            Map maps = (Map) JSON.parse(jsonStr);
            changer.replaceBookMark(maps);

            return "0";
        } catch (InvalidFormatException ife) {
            ife.printStackTrace();
            return "1";
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
            return "2";
        } catch (IOException ie) {
            ie.printStackTrace();
            return "3";
        }
    }

    public static void main(String[] args) {
        String jsonStr = "{\"name\":\"毛文权\",\"sex\":\"男\",\"brithday\":\"2017-11-05\",\"idCard\":\"441823199510164534\",\"cjType\":\"fds\",\"phone\":\"17329958373\",\"youxiaojian\":[\"户口簿\",\"身份证\"],\"houseId\":\"fd\",\"cjNum\":\"fds\",\"jhName\":\"fds\",\"jhRelation\":\"fds\",\"jhPhone\":\"fds\",\"jhAddress\":\"fds\",\"jhIdCard\":\"fds\",\"serviceContent\":\"社会工作服务\",\"sqReason\":\"fds\"}";
//		Map<String,Object> map =InterfaceUtil.parseJSON2Map(jsonStr);
        Map maps = (Map) JSON.parse(jsonStr);
        for (Object map : maps.entrySet()) {
            System.out.println(((Map.Entry) map).getKey() + "     " + ((Map.Entry) map).getValue());
        }
    }


    //	public static void main(String[] args) {
//		long startTime = System.currentTimeMillis();
//		String json = "{\"Principles\":\""
//				+ (char) 0x2612
//				+ "\",\"Purpose\":\"规范会议操作、提高会议质量\",\"img\":{\"w\":50,\"h\":50,\"type\":\"jpg\",\"picpath\":\"C:\\\\0.jpg\"}}";
//
//		MSWordTool changer = new MSWordTool();
//		String one = changer.ChangerForBookmark(json, "E:\\Word.docx",
//				"E:\\Word模版_REPLACE.docx");
//		System.out.println("time==" + (System.currentTimeMillis() - startTime)
//				+ " one " + one);
//	}
//	 
    public String Inserttable(MSWordTool changer, String jsonStr) {

        try {
            Map<String, Object> content = new HashMap<String, Object>();
            content = JsonUtil.parseJSON2Map(jsonStr);
            System.out.println(content.size());
            ;
            for (Map.Entry<String, Object> entry : content.entrySet()) {
                changer.fillTableAtBookMark(entry.getKey(), (List<Map<String, String>>) entry.getValue());
            }
            return "0";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "1";
        }


    }

    public String ChangeAll(String bookmark, String table, String temppath, String savepath) {
        MSWordTool changer = new MSWordTool();

        changer.setTemplate(temppath);

        try {
            if (!"".equals(bookmark)) {
                changer.ChangerForBookmark(changer, bookmark);
            }

            if (!"".equals(table)) {
                changer.Inserttable(changer, table);
            }


            changer.saveAs(savepath);

            return "{error:0,msg:\"转换成功\"}";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "{error:4,msg:\"" + e + "\"}";
        }
    }

    public String ChangeAll(String bookmark, String table, String temppath, String savepath, Map<String, String> map) {

        MSWordTool changer = new MSWordTool();

        changer.setTemplate(temppath);

        try {

            if (!"".equals(bookmark)) {

                changer.ChangerForBookmark(changer, bookmark);

            }


            if (!"".equals(table)) { //table内容全在bookmark中

                changer.Inserttable(changer, table);

            }

            changer.saveAs(savepath);

            //明文替换 只有表格里边内容进行修改

            changer.replaceText(map, savepath);


            return "{error:0,msg:\"转换成功\"}";

        } catch (Exception e) {

            // TODO: handle exception

            e.printStackTrace();

            return "{error:4,msg:\"" + e + "\"}";

        }

    }


    public String ChangeAllAndImg(String bookmark, String table, String imgs, String temppath, String savepath) {
        MSWordTool changer = new MSWordTool();

        changer.setTemplate(temppath);

        try {
            if (!"".equals(bookmark)) {
                changer.ChangerForBookmark(changer, bookmark);
            }
            if (!"".equals(imgs)) {
                changer.ChangerForBookmark(changer, imgs);
            }

            if (!"".equals(table)) {
                changer.Inserttable(changer, table);
            }

            changer.saveAs(savepath);

            return "{error:0,msg:\"转换成功\"}";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "{error:4,msg:\"" + e + "\"}";
        }
    }


//	public static void main(String[] args) {
//		
//		String bookmarkjson = "";		
//		String tablejson = "{\"familyMembers\":[{\"name\":\"测试1\",\"relation\":\"测试\",\"idCard\":\"123456\",\"is\":\"是\"}, {\"name\":\"测试2\",\"relation\":\"测试\",\"idCard\":\"12355456\",\"is\":\"是\"}]}";
//
//		
//		MSWordTool changer  = new MSWordTool();
////		System.out.println(json);
//
//		String str =  changer.ChangeAll(bookmarkjson, tablejson, "E:\\居住证申请表.docx", "E:\\Word模版_REPLACE.docx");
//		System.out.println(str);
//
//		
//	}

//	public static void main(String[] args) {
//		
//		MSWordTool changer = new MSWordTool();
//		changer.setTemplate("E:\\Word.docx");
//		
//		List<Map<String, String>> content2 = new ArrayList<Map<String, String>>();
//		Map<String, String> table1 = new HashMap<String, String>();
//
//		table1.put("MONTH", "1月份");
//		table1.put("SALE_DEP", "75分");
//		table1.put("TECH_CENTER", "80分");
//		table1.put("CUSTOMER_SERVICE", "85分");
//		table1.put("HUMAN_RESOURCES", "90分");
//		table1.put("FINANCIAL", "95分");
//		table1.put("WORKSHOP", "80分");
//		table1.put("TOTAL", "85分");
//		content2.add(table1);
//		Map<String, String> table2 = new HashMap<String, String>();
//		
//		table2.put("MONTH", "2月份");
//		table2.put("SALE_DEP", "75分");
//		table2.put("TECH_CENTER", "80分");
//		table2.put("CUSTOMER_SERVICE", "85分");
//		table2.put("HUMAN_RESOURCES", "90分");
//		table2.put("FINANCIAL", "95分");
//		table2.put("WORKSHOP", "80分");
//		table2.put("TOTAL", "85分");
//		content2.add(table2);
//		
////		changer.fillTableAtBookMark("Table", content2);
////		changer.saveAs("E:\\Word模版_REPLACE.docx");
//		JSONArray json = JSONArray.fromObject(content2);
//		
//		System.out.println("json:"+json);
//		
//		String json2 = "{\"Principles\":\""
//				+ (char) 0x2612
//				+ "\",\"Purpose\":\"规范会议操作、提高会议质量\",\"img\":{\"w\":50,\"h\":50,\"type\":\"jpg\",\"picpath\":\"C:\\\\0.jpg\"}}";
//		Map<String, Object> content = new HashMap<String, Object>();
//		content = InterfaceUtil.parseJSON2Map(json2);
//		
//		for (Map.Entry<String, Object> entry : content.entrySet()) {
//			System.out.println(entry.getValue().getClass().toString());
//    		
//    		   System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
//    	} 
//		
//		System.out.println(json2);
//	}

    /**
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     * @throws InvalidFormatException
     */

/*	public static void main(String[] args) throws InvalidFormatException,
            FileNotFoundException, IOException {
		long startTime = System.currentTimeMillis();
		MSWordTool changer = new MSWordTool();
		changer.setTemplate("E:\\Word.docx");
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("Principles", "格式规范、标准统一、利于阅览");
		content.put("Purpose", "规范会议操作、提高会议质量");
		content.put("Scope", "公司会议、部门之间业务协调会议");

		content.put("customerName", "**有限公司");
		content.put("address", "机场路2号");
		content.put("userNo", "3021170207");
		content.put("tradeName", "水泥制造");
		content.put("price1", "1.085");
		content.put("price2", "0.906");
		content.put("price3", "0.433");
		content.put("numPrice", "0.675");

		content.put("company_name", "**有限公司");
		content.put("company_address", "机场路2号");

		Map<String, Object> heath = new HashMap<String, Object>();
		heath.put("w", 100);
		heath.put("h", 100);
		heath.put("type", "jpg");
		heath.put("picpath", "E:\\1f6.jpg");
		content.put("img", heath);

		changer.replaceBookMark(content);

		// 替换表格标签
		List<Map<String, String>> content2 = new ArrayList<Map<String, String>>();
		Map<String, String> table1 = new HashMap<String, String>();

		table1.put("MONTH", "1月份");
		table1.put("SALE_DEP", "75分");
		table1.put("TECH_CENTER", "80分");
		table1.put("CUSTOMER_SERVICE", "85分");
		table1.put("HUMAN_RESOURCES", "90分");
		table1.put("FINANCIAL", "95分");
		table1.put("WORKSHOP", "80分");
		table1.put("TOTAL", "85分");

		//for (int i = 0; i < 3; i++) {
			content2.add(table1);
		//}
//			changer.fillTableAtBookMark("Table", content2);
			System.out.println("flinsh!");
			
			Map<String, String> table2 = new HashMap<String, String>();
			
			table2.put("MONTH", "2月份");
			table2.put("SALE_DEP", "75分");
			table2.put("TECH_CENTER", "80分");
			table2.put("CUSTOMER_SERVICE", "85分");
			table2.put("HUMAN_RESOURCES", "90分");
			table2.put("FINANCIAL", "95分");
			table2.put("WORKSHOP", "80分");
			table2.put("TOTAL", "85分");
			content2.add(table2);
		changer.fillTableAtBookMark("Table", content2);
		//changer.fillTableAtBookMark("Table3", content2);

		// 表格中文本的替换
		Map<String, String> table = new HashMap<String, String>();
		table.put("CUSTOMER_NAME", "**有限公司");
		table.put("ADDRESS", "机场路2号");
		table.put("USER_NO", "3021170207");
		table.put("tradeName", "水泥制造");
		table.put("PRICE_1", "1.085");
		table.put("PRICE_2", "0.906");
		table.put("PRICE_3", "0.433");
		table.put("NUM_PRICE", "0.675");
		changer.replaceText(table, "Table2");

		// 保存替换后的WORD
		changer.saveAs("E:\\Word模版_REPLACE.docx");
		System.out.println("time==" + (System.currentTimeMillis() - startTime));

	}*/

}
