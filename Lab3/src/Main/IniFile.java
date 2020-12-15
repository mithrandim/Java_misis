package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IniFile
{

    // Регулярное выражение для выделения секции
    private Pattern  _section  = Pattern.compile( "\\s*\\[([^]]*)\\]\\s*" );
    // Регулярное выражение для выделения ключа и значения
    private Pattern  _keyValue = Pattern.compile( "\\s*([^=]*)=(.*)" );
    // Карта карт
    private Map< String, Map< String, String >>  _entries  = new HashMap<>();

    // Конструктор
    public IniFile(String path ) throws IOException
    {
        load( path );
    }

    // Возвращает карту (метод getter)
    public Map< String, Map< String, String >> getEntries()
    {
        return _entries;
    }

    // Создает карту на основе ини файла по пути, который передается в качестве параметра
    public void load( String path ) throws IOException
    {
        try( BufferedReader br = new BufferedReader( new FileReader( path )))
        {
            String line;
            String section = null;
            while(( line = br.readLine()) != null )
            {
                Matcher m = _section.matcher( line );
                if( m.matches()) {
                    section = m.group( 1 ).trim();
                }
                else if( section != null )
                {
                    m = _keyValue.matcher( line );
                    if( m.matches())
                    {
                        String key   = m.group( 1 ).trim();
                        String value = m.group( 2 ).trim();
                        Map< String, String > kv = _entries.get( section );
                        if( kv == null )
                        {
                            _entries.put( section, kv = new HashMap<>());
                        }
                        kv.put( key, value );
                    }
                }
            }
        }
    }

    // Возвращает значение по хэшу (во внутренней карте)
    public String getString( String section, String key, String defaultvalue )
    {
        Map< String, String > kv = _entries.get( section );
        if( kv == null )
        {
            return defaultvalue;
        }
        return kv.get( key );
    }
}

class Main
{
    public static void main(String args[])
    {
        try
        {
            // В качестве пути указано только имя файла, т.е. .ini файл должен лежать в папке с программой
            // Сделано для простоты, так как файл лежит вместе с программой на GitHub
            IniFile ini = new IniFile("sample.ini");

            // Проход по всем ключам (картам внутри) карты
            for (String sector : ini.getEntries().keySet())
            {
                System.out.println("Sector: " + sector);
                // Проход по всем ключам внутренней карты
                for (String key : ini.getEntries().get(sector).keySet())
                {
                    System.out.println(key + " = " + ini.getString(sector, key, "No such sector"));
                }
                System.out.println('\n');
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
