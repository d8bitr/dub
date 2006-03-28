package net.sf.dub.application.sqlmore;

import java.sql.*;
import java.io.*;

/**
 * Handler for data exports
 *
 * Copyright (c) 2001-2004 Marc Lavergne. All rights reserved.
 *
 * The following products are free software; Licensee can redistribute and/or 
 * modify them under the terms of	the GNU Lesser General Public License
 * (http://www.gnu.org/copyleft/lesser.html) as published by the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-	1307  USA; 
 * either version 2.1 of the	license, or any later version:
 */
public class Export
{
  // ***************************************************************************
  
  protected boolean exportDIF(Connection p_conn, String p_sql)
  throws SQLException
  {
    PreparedStatement v_stmt = null;
    ResultSet v_rset = null;
    
    v_stmt = p_conn.prepareStatement(p_sql);
    v_rset = v_stmt.executeQuery();
    
    boolean v_rc = exportDIF(v_rset);
    
    v_rset.close();
    v_stmt.close();
    
    v_rset = null;
    v_stmt = null;
    
    return v_rc;
  }

  // ***************************************************************************
    
  private boolean exportDIF(ResultSet p_rset)
  throws SQLException
  {
    ResultSetMetaData v_meta = p_rset.getMetaData();
    int v_cols = v_meta.getColumnCount();
    
    StringBuffer v_data = new StringBuffer(v_cols *1024);
//    v_data.append("LABEL\n");
    v_data.append("DATA\n0,0\n\"\"\n"); //$NON-NLS-1$
    
    // print out the column headings
    v_data.append("-1,0\nBOT\n"); //$NON-NLS-1$
    for (int i=0; i<v_cols; i++)
    {
//      v_data.append(i+",0\n\""+v_meta.getColumnName(i+1)+"\",\"\"\n");
      v_data.append("1,0\n\""+v_meta.getColumnName(i+1)+"\"\n"); //$NON-NLS-1$ //$NON-NLS-2$
    }

//    v_data.append("DATA\n0,0\n\"\"\n");
    
    // print out the query results
    int v_rcnt = 0;
    while (p_rset.next())
    {
      v_rcnt++;
      v_data.append("-1,0\nBOT\n"); //$NON-NLS-1$
      for (int i=0; i<v_cols; i++)
      {
        switch (v_meta.getColumnType(i+1))
        {
          case java.sql.Types.INTEGER:
          case java.sql.Types.NUMERIC:
            v_data.append("0,"+p_rset.getString(i+1)+"\nV\n");  //$NON-NLS-1$//$NON-NLS-2$
          break;
          default:
            v_data.append("1,0\n\""+p_rset.getString(i+1)+"\"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
      }
    }

    v_data.append("-1,0\nEOD\n"); //$NON-NLS-1$
    write(v_cols, v_rcnt, v_data);
    
    return true;
  }
  
  // ***************************************************************************
  
  private void write(int p_cols, int p_rows, StringBuffer p_data)
  {
    try
    {
      java.io.FileOutputStream v_file = new java.io.FileOutputStream("dbplus.dif"); //$NON-NLS-1$
      
      write(v_file, "TABLE\n0,1\n\"EXCEL\"\nVECTORS\n0,"+p_cols+"\n\"\"\nTUPLES\n0,"+p_rows+"\n\"\"\n");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
      write(v_file, p_data.toString());
      
      v_file.close();
      
      System.out.println("Exported to file dbplus.dif"); //$NON-NLS-1$
    }
    catch (java.io.IOException e_io)
    {
    	System.out.println(e_io.toString());
    }
  }
  
  // ***************************************************************************
  
  private static void write(java.io.FileOutputStream p_file, String p_string)
  throws IOException
  {
    p_file.write(p_string.getBytes());
  }
  
  // ***************************************************************************
}