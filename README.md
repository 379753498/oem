oem
===

This is a tool for excel mapping to pojo

����֮ǰд��һ����excel ת���ɶ���Ĺ��߰�,ת����maven��

ʹ��˵��(����ֻ֧��excel 2007):
	
	ExcelToObjectByPoi reader=new ExcelToObjectByPoi(ExcelToObjectTest.class.getResource("/"));  

	@Test
	public void testReadExcel() throws InvalidFormatException, IOException {
		File excelFile = new File(ExcelToObjectTest.class.getResource("/test.xlsx").getFile());
		Workbook workbook = WorkbookFactory.create(excelFile);
		List<User> users = reader.readSheet(workbook.getSheetAt(0), User.class);
		System.out.println(users);
	}