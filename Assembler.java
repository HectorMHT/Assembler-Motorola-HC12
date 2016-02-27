import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JOptionPane;
//@Author Héctor Huerta Toledo
public class Assembler {
String path="", errPath="", validPath="";
private static Interface current;
    
    
    void ensamblar(Interface current,String path, String errPath, String validPath){
        int i=0;
        int end=0;
        Assembler.setCurrent(current);
        this.path=path;
        this.errPath=errPath;
        this.validPath=validPath;
        try{
            RandomAccessFile archivo=new RandomAccessFile(path, "r");
            String linea="";
            int err=0, valid=0;
            while(archivo.length()!=archivo.getFilePointer()){
                linea=archivo.readLine();
                if(!linea.equals("\n")&&!linea.equals("\r\n")){                    
                    Line line=new Line(linea,i+1,err,valid, errPath, validPath,current);
                    line.quitarComentarios();
                    line.analizar();
                    if(line.contadorError>0) err++;
                    if(line.contadorValidas>0) valid++;
                    if(line.end==1){
                        end=1;
                        break;
                    }
                    i++;
                }                
            }
            if(end==0) errorEnd();
            archivo.close();
            JOptionPane.showMessageDialog(null, "program assembly! \n Lines:"+i);
        }catch(IOException e){
        	JOptionPane.showMessageDialog(null, "Erro trying to open file .asm",null,JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    void errorEnd(){
    	String row[]=new String[3];
        String cadena="Sentence END is missing!" ;
        row[0]="We have an Error";
        row[1]="Missing";
        row[2]=cadena;
        current.Add_mess_row(row);
        try{
            RandomAccessFile archivo=new RandomAccessFile(this.errPath, "rw");
            archivo.seek(archivo.length());
            archivo.writeBytes(cadena+"\r\n");
            archivo.close();
        }catch(IOException e){
        	JOptionPane.showMessageDialog(null, "Erro trying to open file .err",null,JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args){
        setCurrent(new Interface());       
    }

	public static Interface getCurrent() {
		return current;
	}

	public static void setCurrent(Interface current) {
		Assembler.current = current;
	}

}
