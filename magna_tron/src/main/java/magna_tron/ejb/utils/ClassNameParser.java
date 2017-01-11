
package magna_tron.ejb.utils;

/**
* <h1>ClassNameParser</h1>
* Questa classe manipola il nome di una classe.
*/
public class ClassNameParser {
          /**
           * @param className nome completo della classe
           * @return nome della classe senza package
           */
          public static String splitClassName(String className){
                 String[] output=className.split("\\.");
                 String name=output[output.length-1];
                 return name;
          }
}
