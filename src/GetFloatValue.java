/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author doxu
 */
public class GetFloatValue {
          private String sname ;
          public String getSname() {
          return sname;
        }

        public void setSname(String name) {
          this.sname = name;
        }

    /**
     *
     */
         public float getvalue(String s)
        {
            String m[]= s.split("/"); 
            float a=1;
            float d[]= new float[m.length];
            GetFloatValue[] st = new GetFloatValue[m.length];//定义一个实体类数组
            for(int i =0;i<m.length;i++)
            { 
                st[i]=new GetFloatValue();
                st[i].setSname(m[i]); //sname赋值
                // System.out.println(st[i].getSname());
                d[i] =  Float.parseFloat(st[i].getSname());
                a=d[i]*a;
                //System.out.println(d[i]);
            } 
            return a;
        
       }
        
      }
