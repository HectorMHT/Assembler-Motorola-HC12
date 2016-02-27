

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
//@Author Héctor Huerta Toledo
public class Line{
	  
    String linea;
    String etq;
    String codop;
    String opr;
    String errPath;
    String validPath;
    int numberOfTokens;
    int numLinea;
    int numError=0;    
    int end=0;
    int contadorValidas=0;
    int contadorError=0;
    Interface current;
    
    public Line(String linea, int num, int contErr, int contValid, String errPath, String validPath, Interface current){
        this.linea=linea;
        this.numLinea=num;
        this.contadorError=contErr;
        this.contadorValidas=contValid;
        this.errPath=errPath;
        this.validPath=validPath;
        this.current=current;
    }
    
    public void quitarComentarios(){
        String buff="";
        char car;
        for(int i=0;i<this.linea.length();i++){
            car=this.linea.charAt(i);
            if(car!=';'){
                buff=buff+car;
            }else break;            
        }
        this.linea=buff;
    }    
   
    public void analizar(){
        if(!this.linea.isEmpty()&&!this.linea.equals(" ")&&revisarLinea()){
            separarTokens();
            if(this.numError==0){
                if(!this.codop.equalsIgnoreCase("end")){
                    if(validarEtq()&&validarCodOp()&&validarOper()){
                        lineaValida();
                    }
                    else mandarError();
                }else{
                    lineaValida();
                    this.end=1;
                }
            }else mandarError();
        }else return;
    }
    
    boolean revisarLinea(){
        boolean resul=false;
        for(int i=0;i<this.linea.length();i++){
            if((int)this.linea.charAt(i)==32||(int)this.linea.charAt(i)==9){
                resul=false;
            }
            else{
                resul=true;
                break;
            }
        }
        return resul;
    }
    
    void separarTokens(){        
        StringTokenizer tokens=new StringTokenizer(this.linea);
        if(tieneEtiqueta()){
            switch(tokens.countTokens()){
                case 1: this.numError=1;
                    break;
                case 2:
                    this.etq=tokens.nextToken();
                    this.codop=tokens.nextToken();
                    this.opr="NULL";
                    break;
                case 3:
                    this.etq=tokens.nextToken();
                    this.codop=tokens.nextToken();
                    this.opr=tokens.nextToken();
                    break;
                default:
                    this.numError=1;                    
            }            
        }else{
            switch(tokens.countTokens()){
                case 1:
                    this.etq="NULL";
                    this.codop=tokens.nextToken();
                    this.opr="NULL";
                    break;
                case 2:
                    this.etq="NULL";
                    this.codop=tokens.nextToken();
                    this.opr=tokens.nextToken();
                    break;
                default:
                    this.numError=1;
            }
        }
    }    
    
    boolean tieneEtiqueta(){
        char car=this.linea.charAt(0);
        if(car=='\t'||car=='\n'||car=='\r'||car=='\f'||(int)car==32||(int)car==9)
        	return false;
        else 
        	return true;                               
    }
    
    boolean validarEtq(){
        int estado=0;
        boolean retorno=false;
        if(this.etq.equals("NULL")) return true;
        else{
            if (this.etq.length()<9){
                for(int i=0;i<this.etq.length();i++){
                    switch(i){
                        case 0:
                            if((this.etq.charAt(i)>='A'&&this.etq.charAt(i)<='Z')||(this.etq.charAt(i)>='a'&&this.etq.charAt(i)<='z')){
                                estado++;
                            }
                            else estado=10;
                            break;
                        case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                            if((this.etq.charAt(i)>='A'&&this.etq.charAt(i)<='Z')||(this.etq.charAt(i)>='a'&&this.etq.charAt(i)<='z')
                                    ||this.etq.charAt(i)=='_'||(this.etq.charAt(i)>='0'&&this.etq.charAt(i)<='9')){
                                estado++;
                            }
                            else estado=10;
                            break;
                        
                    }
                    if(estado==10) break;
                }
                switch(estado){
                    case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8:
                        retorno=true;
                        break;
                    case 10:
                        retorno=false;
                        this.numError=2;
                }
            }else{
                this.numError=2;
                retorno=false;
            }
        }
        return retorno;
    }
    
    boolean validarCodOp(){
        int puntos=0;
        int estado=0;
        boolean retorno=false;
        if(this.codop.length()<6){
            for(int i=0;i<this.codop.length();i++){
                switch(i){
                    case 0:
                        if((this.codop.charAt(i)>='A'&&this.codop.charAt(i)<='Z')||(this.codop.charAt(i)>='a'&&this.codop.charAt(i)<='z')){
                            estado++;
                        }
                        else{
                            estado=10;
                        }
                        break;
                    case 1: case 2: case 3: case 4:
                        if((this.codop.charAt(i)>='A'&&this.codop.charAt(i)<='Z')||(this.codop.charAt(i)>='a'&&this.codop.charAt(i)<='z')){
                            estado++;
                        }else if(this.codop.charAt(i)=='.'){
                            puntos++;
                            estado++;
                        }else estado=10;
                        break;                                      
                }
                if(estado==10) break;
            }
            switch(estado){
                case 1: case 2: case 3: case 4: case 5:
                    retorno=true;
                    break;
                case 10:
                    this.numError=3;
                    retorno=false;
            }
            if (puntos>1){
                this.numError=3;
                retorno=false;                
            }
        }else{
            this.numError=3;
            retorno=false;
        } 
        return retorno;
    }
    
    boolean validarOper(){
        return true;        
    }
    
    void lineaValida(){
        String cadena[]=new String[4];
        		cadena[0]=String.valueOf(this.numLinea);
        		cadena[1]=this.etq;
        		cadena[2]=this.codop;
        		cadena[3]=this.opr;
        current.Add_row(cadena);
        try{
            RandomAccessFile archivo=new RandomAccessFile(this.validPath,"rw");
            if(this.contadorValidas==0){
                archivo.setLength(0);
                String inicio="Linea	ETQ	CODOP		OPER\r\n.................................................\r\n";
                archivo.writeBytes(inicio);
            } 
            archivo.seek(archivo.length());
            archivo.writeBytes(cadena+"\r\n");            
            archivo.close();
            this.contadorValidas++;
        }catch(IOException e){
        	JOptionPane.showMessageDialog(null, "Erro trying to open file .inst",null,JOptionPane.ERROR_MESSAGE);
        }        
    }
    
    void mandarError(){
    	String row[]=new String[3];
        String error="";
        String cadena;
        String inicio="Line	Descrition\r\n....................................................\r\n";
        switch(this.numError){
            case 1:
                error="Invalid Statements number\n";
                break;
            case 2:
                error="Invalid label format\n";
                break;
            case 3:
                error="Invalid format in operation code\n";
        }
        cadena=String.valueOf(this.numLinea)+"\t"+error;
        
        row[0]="We have an Error!";
        row[1]=String.valueOf(this.numLinea);
        row[2]=error;
       
        current.Add_mess_row(row);
       
        try{
            RandomAccessFile archivo=new RandomAccessFile(this.errPath, "rw");
            if(this.contadorError==0){
                archivo.setLength(0);
                archivo.writeBytes(inicio);
            }
            archivo.seek(archivo.length());
            archivo.writeBytes(cadena+"\r\n");
            archivo.close();
            this.contadorError++;

            
        }catch(IOException e){
			JOptionPane.showMessageDialog(null, "Error trying to open .err File",null,JOptionPane.ERROR_MESSAGE);
        }
        
    }
}
