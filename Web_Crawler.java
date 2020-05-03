/*
 * Title : Application of Web Crawler and Web Indexing 
 * Data structure used : Graphs , HashMap
*/
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.net.*;
import javax.swing.*;
class Crawler
{
	String seed="https://www.cumminscollege.org";		//seed url
	HashMap<String,ArrayList<String>> index=new HashMap<>();
	//+ pattern can occur one to many times
	// /w represents word character * indicates s is optional
	void bfs()
	{
		System.out.println("URL'S BROWSED\n");
		int urlcnt=0;
		String regex="http[s]*://(\\w+\\.)*(\\w+)";
		Queue<String> q=new LinkedList<String>();

		Set<String> visited=new HashSet<String>();		//Stores if web site already visited
		int cnt=2;								//cnt indicates levels to be traversed

		BufferedReader br=null;		//Reading text of web page from character stream
		q.add(seed);
		visited.add(seed);
		q.add(null);
		while(cnt!=0 && !q.isEmpty())
		{

			String url=q.remove();
			if(url==null)
			{
				if(!q.isEmpty())
				{
					q.add(null);

				}
				cnt--;
			}
			else
			{
				String content="<title>(.*)</title>";
				String s;
				try
				{
					URL ul=new URL(url);
					//openStream: Opens a connection to this URL and returns an InputStream for reading from that connection.
					br=new BufferedReader(new InputStreamReader(ul.openStream()));


					StringBuilder sb=new StringBuilder();



					while((s=br.readLine())!=null)
					{
						sb.append(s);
					}
					s=sb.toString();
					Pattern pat=Pattern.compile(content);
					Matcher matcher=pat.matcher(s);
					Pattern pattern=Pattern.compile(regex);	 //pattern:used to get regular expression which is compiled to create pattern.
					Matcher match=pattern.matcher(s);		//Matcher class is used to find and store occurances
					while(match.find())			//returns true if next subsequence matching the pattern is found
					{
						String hpl=match.group();
						//returns subsequence matching the pattern
						if(!visited.contains(hpl))	//If hyperlink not visited
						{
							visited.add(hpl);		
							urlcnt++;
							System.out.println(hpl);
							q.add(hpl);
						}
					}
					if(matcher.find())
					{
						String title=matcher.group();
						for(String words : title.split(" "))		//split words of the title
						{
							if(words.contains("<title>"))
							{
								words=words.substring(7);
							}
							if(words.contains("</title>"))
							{
								words=words.substring(0,(words.length()-8));
							}

							words = words.replaceAll("[^a-zA-Z0-9\\s+]", "");	//remove special characters
							words=words.toLowerCase();		
							if(index.containsKey(words))
							{
								ArrayList<String> a=index.get(words);
								if(!a.contains(url))		//Url should be present un the search only once
								{
									a.add(url);
								}
							}
							else
							{
								ArrayList<String> temp=new ArrayList<>();
								temp.add(url);
								index.put(words, temp);
							}
						}
					}

				}
				catch(Exception e)
				{
					
				}


			}
		}
		System.out.println("\n");
		System.out.println("URL's searched: "+(visited.size()-1)+"\n");
		System.out.println("================================");
	}
	void display()			//displays word index
	{
		System.out.println("\t--------------------");
		System.out.println("\n\t\tINDEX\n");
		System.out.println("\t--------------------");
		for(String key:index.keySet())
		{
			ArrayList<String> temp=index.get(key);
			System.out.println("\n"+key+" : ");
			for(int i=0;i<temp.size();i++)
			{
				System.out.println("\t\t"+temp.get(i));
			}
			System.out.println("\n--------------------------------------------------------\n");
		}
		
	}
	ArrayList<String> search(String search)		//searches urls according to given string
	{
		ArrayList<String> result=new ArrayList<String>();
		for(String words:search.split(" "))
		{
			if(words.length()>2 && index.containsKey(words))
			{
				ArrayList<String> temp=index.get(words);
				for(int i=0;i<temp.size();i++)
				{
					if(!result.contains(temp.get(i)))
					{
						result.add(temp.get(i));
					}
				}
			}
		}
		return result;
	}

}
class SearchBox extends JFrame {
	private JTextField searchable = new JTextField(30);		//To enter the string
	private JButton searchB = new JButton("Search");		//search button
	private JTextArea tarea=new JTextArea(10,10);	//Area to display search results
	private JPanel panel = new JPanel();			//Panel to hold contents
	protected String str;

	SearchBox(String title) throws HeadlessException {		//Headless exception:if Environment doesn't support mouse 
		super(title);
		Font font1 = new Font("SansSerif", Font.BOLD, 20);		//font
		searchable.setPreferredSize(new Dimension(10,50));
		searchable.setFont(font1);
		searchB.setPreferredSize(new Dimension(100, 40));
		searchB.setFont(font1);
		tarea.setPreferredSize(new Dimension(800,800));
		tarea.setFont(font1);
		tarea.setEditable(false);
		setSize(700, 700);
		setResizable(false);		//user is not allowed to resize frame
		addComponents();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//closes the running app
		setVisible(true);

	}
	private void addComponents() {			//add all components in the panel
		panel.add(searchable);
		panel.add(searchB);
		panel.add(tarea);
		add(panel);
	}
	void setTable(final Crawler cw)
	{

		searchB.addActionListener(new ActionListener(){		//Search button on mouse click
			public void actionPerformed(ActionEvent evt)
			{
				str=searchable.getText();
				str=str.toLowerCase();
				displayLabel(str,cw);		

			}

		});
	}
	void displayLabel(String s,Crawler cw)		//display search results in the Text field
	{
		ArrayList<String> r=cw.search(str);
		if(r.size()==0)		//validation
		{
			tarea.setText("\n\t\tNo results found!");	
		}
		else
		{
			String app = "\n\t";
			for(int i=0;i<r.size();i++)
			{
				app=app+r.get(i)+"\n\n\t";

			}
			tarea.setText(app);
		}


	}

}
public class Web_Crawler {
	public static void main(String[] args)
	{
		int ch=0;
		Crawler c=new Crawler();
		c.bfs();
		c.display();
		SearchBox s=new SearchBox("Bingo");
		s.setTable(c);
		
	}
}


