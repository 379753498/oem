oem
===

This is a tool for excel mapping to pojo

����֮ǰд��һ����excel ת���ɶ���Ĺ��߰�,ת����maven��

ʹ��˵��(����ֻ֧��excel 2007):
	
	ExcelToObject reader=new ExcelToObjectByPoi();  

	@Test
	public void testReadExcel() {
		String filePath = new File(ExcelToObjectTest.class.getResource("/test.xlsx").getFile()).getPath();
		List<User>[] users = reader.readExcel(filePath, new Class[] {User.class});
		User user = users[0].get(0);
		System.out.println(user);
	}