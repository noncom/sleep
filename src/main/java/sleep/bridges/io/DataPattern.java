/* 
 * Copyright (C) 2002-2012 Raphael Mudge (rsmudge@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package sleep.bridges.io;

import java.util.*;
import java.nio.*;

/** A DataPattern represents a data format for Sleep's IO functions. */
public class DataPattern
{
   public DataPattern next  = null;
   public int         count = 1;
   public char        value = ' ';
   public int         size  = 0;
   public ByteOrder   order = ByteOrder.BIG_ENDIAN;

   private static HashMap patternCache = new HashMap();

   public static int EstimateSize(String format)
   {
      DataPattern pattern = Parse(format);

      int count = 0;

      while (pattern != null)
      {
         if (pattern.count > 0)
           count += pattern.count * pattern.size;

         pattern = pattern.next;
      }

      return count;
   }

   public static DataPattern Parse(String format)
   {
      if (patternCache.get(format) != null)
          return (DataPattern)patternCache.get(format);

      DataPattern head   = null, temp = null;
      StringBuffer count = null;

      for (int x = 0; x < format.length(); x++)
      {
         if (Character.isLetter(format.charAt(x)))
         {
            if (temp != null)
            {
               if (count.length() > 0)
                  temp.count = Integer.parseInt(count.toString());

               temp.next = new DataPattern();
               temp      = temp.next;

            }
            else
            {
               head      = new DataPattern();
               temp      = head;
            }

            count = new StringBuffer(3);
            temp.value = format.charAt(x);

            switch (temp.value)
            {
               case 'b':
               case 'B':
               case 'C':
               case 'h':
               case 'H':
               case 'x':
               case 'o':
                 temp.size = 1;
                 break;
               case 'u':
               case 'U':
                 temp.count = -1;
                 temp.size = 2;
                 break;
               case 'z':
               case 'Z':
                 temp.count = -1;
                 temp.size = 1;
                 break;
               case 'c':
               case 's':
               case 'S':
                 temp.size = 2;
                 break;
               case 'i':
               case 'I':
               case 'f':
                 temp.size = 4;
                 break;
               case 'd':
               case 'l':
                 temp.size = 8;
                 break;  
            }
         }
         else if (format.charAt(x) == '*')
         {
            temp.count = -1;
         }
         else if (format.charAt(x) == '!')
         {
            temp.order = ByteOrder.nativeOrder();
         }
         else if (format.charAt(x) == '-')
         {
            temp.order = ByteOrder.LITTLE_ENDIAN;
         }
         else if (format.charAt(x) == '+')
         {
            temp.order = ByteOrder.BIG_ENDIAN;
         }
         else if (Character.isDigit(format.charAt(x)))
         {
            count.append(format.charAt(x));
         }
      }

      if (count.length() > 0)
         temp.count = Integer.parseInt(count.toString());

      patternCache.put(format, head);
      return head;
   }
}
