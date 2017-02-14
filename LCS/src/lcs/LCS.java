/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcs;

/**
 *
 * @author Dong
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
public class LCS 
{
    //lcs checker
    public String lcs(String str1, String str2)
    {
        //get the length of boths strings for comparison
        int l1 = str1.length();
        int l2 = str2.length();
 
        int[][] arr = new int[l1 + 1][l2 + 1];
 
        for (int i = l1 - 1; i >= 0; i--)
        {
            for (int j = l2 - 1; j >= 0; j--)
            {
                if (str1.charAt(i) == str2.charAt(j))
                    arr[i][j] = arr[i + 1][j + 1] + 1;
                else 
                    arr[i][j] = Math.max(arr[i + 1][j], arr[i][j + 1]);
            }
        }
 
        int i = 0, j = 0;
        StringBuffer sb = new StringBuffer();
        while (i < l1 && j < l2) 
        {
            if (str1.charAt(i) == str2.charAt(j)) 
            {
                sb.append(str1.charAt(i));
                i++;
                j++;
            }
            else if (arr[i + 1][j] >= arr[i][j + 1]) 
                i++;
            else
                j++;
        }
        return sb.toString();
    }
    
    public static void main(String[] args) throws IOException
    {    
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Longest Common Subsequence Algorithm Test\n");
 
        System.out.println("\nEnter string 1");
        String str1 = "prudence";
 
        System.out.println("\nEnter string 2");
        String str2 = "providence";
 
        LCS obj = new LCS(); 
        String result = obj.lcs(str1, str2); 
        System.out.println("\nLongest Common Subsequence : "+ result);
        
        
        System.out.println("\nNow testing our algorithm...");        
        LCS_API myApi = new LCS_API();
        result = myApi.findLcs(str1,str2,10,100,.75,.20,.05);
        System.out.println("\nLongest Common Subsequence : "+ result);
    }
}
