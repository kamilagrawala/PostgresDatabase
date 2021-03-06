import org.omg.CORBA.SystemException;
import java.sql.*;
import java.util.Scanner;
import org.json.JSONArray;

public class Instructions {

	public boolean getResult(String instruction) {
		Connection con = null;
		ResultSet rs = null;
		Statement stmt = null;
		// System.out.println("-------- PostgreSQL "
		// + "JDBC Connection Testing ------------");
		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return false;
		}

		try {

			con = DriverManager.getConnection(
					"jdbc:postgresql://192.168.56.101:5432/localdev_db",
					"kagrawala", "12qazWSX");

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		}

		if (con != null) {
			// System.out.println("Database controllable now!");
			try {
				instruction = instruction.toLowerCase();
				switch (instruction) {
				case "insert":
					Scanner scan = new Scanner(System.in);;
					String name;
					String swiftcode;
					System.out.printf("\nName of Bank?\n");
					name = scan.nextLine().trim();
					System.out.printf("\nSwiftCode?\n");
					swiftcode = scan.nextLine().trim();
					if(name==null || swiftcode==null || name.equalsIgnoreCase("") ||swiftcode.equalsIgnoreCase("")){
						System.out.println("You must provide a Bank Name and SwiftCode!");
						break;
					}
					System.out.println("Trying INSERT");
					String insert_stament = "INSERT INTO public.\"Banks\"(\"Name\", \"swiftcode\") VALUES('"
							+ name + "'," + "'" + swiftcode + "')";
					//System.out.printf("%s", insert_stament);
					executeStatement(con, stmt, rs, instruction, insert_stament);
					break;
				case "select":
					System.out.println("Trying SELECT");
					executeStatement(con, stmt, rs, instruction,
							"Select * from public.\"Banks\"");
					break;
				case "delete":
					System.out.println("Trying DELETE");
					scan = new Scanner(System.in);;
					System.out.println("Provide Id");
					String id = scan.nextLine().trim();		
					String delete_statement = "DELETE FROM public.\"Banks\" where public.\"Banks\".\"ID\"="+id;
					System.out.println(delete_statement);
					executeStatement(con, stmt, rs, instruction, delete_statement);
					break;
				case "update":
					System.out.println("Trying UPDATE");
					break;
				case "exit":
					try {
						con.close();
						System.out.println("GoodBye!");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return false;
				default:
					System.out.println("Unknown Command");
					break;
				}
			} catch (SystemException e) {

				System.out.println("Connection Failed! Check output console");
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			System.out.println("Failed to make connection!");
			return false;
		}
	}

	private ResultSet executeStatement(Connection con, Statement stmt,
			ResultSet rs, String instruction, String query) {
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 1);
			if (instruction.equalsIgnoreCase("INSERT") || instruction.equalsIgnoreCase("DELETE")) {
				stmt.executeUpdate(query); // returns int but not used
			} else {
				rs = stmt.executeQuery(query);
			}
			if (rs != null) {
				System.out.println();
				loopAndPrintResult(rs);
				// Back to first row
				rs.beforeFirst();
				getJsonObject(rs);
				System.out.printf("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	private void loopAndPrintResult(ResultSet rs) {
		// System.out.println("Inside loopAndPrint()");
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			StringBuilder sb_coloumn = new StringBuilder();
			StringBuilder sb_row = new StringBuilder();
			System.out.println();
			for (int i = 1; i <= columnsNumber; i++) {
				sb_coloumn.append(String.format("| %-30s",
						rsmd.getColumnLabel(i)));
			}
			System.out.println(sb_coloumn);
			rs.beforeFirst();
			while (rs.next()) {
				sb_row.append(String.format("| %-30s|%-31s|%s\n",
						rs.getObject("name").toString().trim(),
						rs.getObject("swiftcode").toString().trim(), rs
								.getObject("id").toString()));
				sb_row.append(String
						.format("--------------------------------------------------------------------------------\n"));
			}
			System.out.println(sb_row);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public JSONArray getJsonObject(ResultSet rs) {
		JSONArray jasonArray;
		try {
			jasonArray = Convertor.convertToJSON(rs);
			System.out.print(jasonArray.toString());
			return jasonArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getXml(ResultSet rs) {
		String xml;
		try {
			xml = Convertor.convertToXML(rs);
			System.out.print(xml);
			return xml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
