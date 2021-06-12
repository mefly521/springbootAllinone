/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package poi.excel.examples.html;

import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Formatter;

/**
 * This interface is used where code wants to be independent of the workbook
 * formats.  If you are writing such code, you can add a method to this
 * interface, and then implement it for both HSSF and XSSF workbooks, letting
 * the driving code stay independent of format.
 *
 * @author Ken Arnold, Industrious Media LLC
 */
public interface HtmlHelper {
    /**
     * Outputs the appropriate CSS style for the given cell style.
     *
     * @param style The cell style.
     * @param out   The place to write the output.
     */
    void colorStyles(CellStyle style, Formatter out);
}
