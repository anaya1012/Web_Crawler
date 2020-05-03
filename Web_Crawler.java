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
/*OUTPUT
 * URL'S BROWSED

https://gmpg.org
https://api.w.org
http://schema.org
https://www.facebook.com
https://twitter.com
https://www.youtube.com
https://www.googletagmanager.com
https://schema.org
http://www.cumminscollege.org
https://www.outlookindia.com
https://cumminscollege.truecopy.in
http://172.16.1.121
https://www.youtube
https://cumminscollege.org
https://www.binary.net.in
http://www.w3.org
http://gmpg.org
https://developer.wordpress.org
https://fonts.googleapis.com
https://wordpress.org
https://codex.wordpress.org
https://login.wordpress.org
https://github.com
https://en.wikipedia.org
https://wordpressfoundation.org
https://publiccode.eu
https://s.w.org
https://gravatar.com
https://m.facebook.com
https://ar
https://bg
https://bs
https://ca
https://da
https://el
https://es
https://fa
https://fi
https://fr
https://hi
https://hr
https://id
https://it
https://ko
https://mk
https://ms
https://pl
https://pt
https://ro
https://sl
https://sr
https://th
https://vi
https://static.xx.fbcdn.net
https://ur
https://gu
https://kn
https://ml
https://te
https://bn
https://ta
https://pa
https://en
https://messenger.com
https://l.facebook.com
https://developers.facebook.com
https://facebook.com
https://cx.atdmt.com
https://pixel.facebook.com
https://abs.twimg.com
https://pbs.twimg.com
https://t.co
https://mobile.twitter.com
http://www.bohemiancoding.com
http://m.twitter.com
https://blog.twitter.com
http://status.twitter.com
https://about.twitter.com
https://marketing.twitter.com
https://business.twitter.com
http://support.twitter.com
https://dev.twitter.com
https://m.youtube.com
https://s.ytimg.com
https://plus.google.com
https://accounts.google.com
https://yt3.ggpht.com
https://i.ytimg.com
https://www.google.com
https://cse.google.com
https://www.w3.org
http://lists.w3.org
http://github.com
http://blog.schema.org
http://www.outlookindia.com
https://cdnjs.cloudflare.com
https://www.googletagservices.com
https://sb
http://b
http://b.scorecardresearch.com
https://cdn.izooto.com
https://s.go
https://s2.go
https://poshan.outlookindia.com
http://subscription.outlookindia.com
https://subscription.outlookindia.com
https://www.outlookhindi.com
https://www.outlookbusiness.com
https://www.responsibletourismindia.com
http://speakout.outlookindia.com
http://www.instagram.com
https://images.outlookindia.com
http://emagazine.outlookindia.com
https://casino.betway.com
http://www.books.outlookindia.com
http://www.osm
https://maxcdn.bootstrapcdn.com
http://www.truecopy.in
http://www.binary.net.in
https://www
http://www.bybuilders.in
http://www.simer.in
http://www.smsbarwani.com
http://thekalyanischool.com
http://www.akshudyog.com
https://ajax.googleapis.com
https://www.members.nettcode.com
https://www.linkedin.com
https://www.google
https://v2.zopim.com
https://cdn.userway.org


URL's searched: 131

================================
	--------------------

		INDEX

	--------------------

 : 
		https://api.w.org
		https://twitter.com
		https://schema.org
		https://www.binary.net.in

--------------------------------------------------------


hrefhttpstwittercomlangenlink : 
		https://twitter.com

--------------------------------------------------------


hreflangca : 
		https://twitter.com

--------------------------------------------------------


hreflogin : 
		https://twitter.com

--------------------------------------------------------


namemsapplicationtilecolor : 
		https://twitter.com

--------------------------------------------------------


classstaticloggedouthomepagecell : 
		https://twitter.com

--------------------------------------------------------


hreflangsk : 
		https://twitter.com

--------------------------------------------------------


typesubmit : 
		https://twitter.com

--------------------------------------------------------


xmlnsxlinkhttpwwww3org1999xlink : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangrulink : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlanggulink : 
		https://twitter.com

--------------------------------------------------------


hreflangsv : 
		https://twitter.com

--------------------------------------------------------


your : 
		https://twitter.com

--------------------------------------------------------


pweve : 
		https://twitter.com

--------------------------------------------------------


+documentbodygetattributedatafoucclassnames : 
		https://twitter.com

--------------------------------------------------------


hreflangsr : 
		https://twitter.com

--------------------------------------------------------


hreflangbn : 
		https://twitter.com

--------------------------------------------------------


would : 
		https://twitter.com

--------------------------------------------------------


hreftimeline : 
		https://twitter.com

--------------------------------------------------------


valuetrue : 
		https://twitter.com

--------------------------------------------------------


xml : 
		https://twitter.com

--------------------------------------------------------


api : 
		https://api.w.org

--------------------------------------------------------


idswiftpagename : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangdelink : 
		https://twitter.com

--------------------------------------------------------


staticloggedouthomepagecommunicationblock : 
		https://twitter.com

--------------------------------------------------------


welcome : 
		https://gmpg.org

--------------------------------------------------------


you : 
		https://twitter.com

--------------------------------------------------------


breaking : 
		https://twitter.com

--------------------------------------------------------


classnoscriptformcontent : 
		https://twitter.com

--------------------------------------------------------


hreflangro : 
		https://twitter.com

--------------------------------------------------------


classstaticloggedouthomepageloginform : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangarlink : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangeulink : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangzhcnlink : 
		https://twitter.com

--------------------------------------------------------


hreflangbg : 
		https://twitter.com

--------------------------------------------------------


contentabstwimgcomfaviconswin8tile144pngmeta : 
		https://twitter.com

--------------------------------------------------------


hreflangru : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangfalink : 
		https://twitter.com

--------------------------------------------------------


analysis : 
		https://www.outlookindia.com

--------------------------------------------------------


javascript : 
		https://twitter.com

--------------------------------------------------------


0 : 
		https://twitter.com

--------------------------------------------------------


input : 
		https://twitter.com

--------------------------------------------------------


meta : 
		https://twitter.com

--------------------------------------------------------


classloginforminput : 
		https://twitter.com

--------------------------------------------------------


classedgebutton : 
		https://twitter.com

--------------------------------------------------------


happening : 
		https://twitter.com

--------------------------------------------------------


hrefandroidappcomtwitterandroidtwitterfrontrefsrctwsrc5egoogle7ctwcamp5eandroidseo7ctwgr5ehomelink : 
		https://twitter.com

--------------------------------------------------------


classnoscriptform : 
		https://twitter.com

--------------------------------------------------------


hreflangar : 
		https://twitter.com

--------------------------------------------------------


it39s : 
		https://twitter.com

--------------------------------------------------------


hreflangur : 
		https://twitter.com

--------------------------------------------------------


staticloggedouthomepagebuttonsignup : 
		https://twitter.com

--------------------------------------------------------


entertainment : 
		https://twitter.com
		https://www.outlookindia.com

--------------------------------------------------------


datafoucclassnamesswiftloading : 
		https://twitter.com

--------------------------------------------------------


pune : 
		https://www.cumminscollege.org
		https://cumminscollege.org
		https://www.binary.net.in

--------------------------------------------------------


icon : 
		https://twitter.com

--------------------------------------------------------


jssigninemail : 
		https://twitter.com

--------------------------------------------------------


encodingutf8svg : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlanghelink : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomsignup : 
		https://twitter.com

--------------------------------------------------------


httpwwwbohemiancodingcomsketch : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlanguklink : 
		https://twitter.com

--------------------------------------------------------


hreflangengb : 
		https://twitter.com

--------------------------------------------------------


hreflanguk : 
		https://twitter.com

--------------------------------------------------------


disabled : 
		https://twitter.com

--------------------------------------------------------


nameredirectafterlogin : 
		https://twitter.com

--------------------------------------------------------


nowh1 : 
		https://twitter.com

--------------------------------------------------------


email : 
		https://twitter.com

--------------------------------------------------------


latest : 
		https://www.outlookindia.com

--------------------------------------------------------


hreflangtr : 
		https://twitter.com

--------------------------------------------------------


hreflangda : 
		https://twitter.com

--------------------------------------------------------


a : 
		https://twitter.com

--------------------------------------------------------


autocompleteusername : 
		https://twitter.com

--------------------------------------------------------


relalternate : 
		https://twitter.com

--------------------------------------------------------


classstaticloggedouthomepagesignupblock : 
		https://twitter.com

--------------------------------------------------------


namemsapplicationtileimage : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangitlink : 
		https://twitter.com

--------------------------------------------------------


mediahandheld : 
		https://twitter.com

--------------------------------------------------------


right : 
		https://twitter.com

--------------------------------------------------------


hreflangde : 
		https://twitter.com

--------------------------------------------------------


the : 
		https://twitter.com

--------------------------------------------------------


p : 
		https://twitter.com

--------------------------------------------------------


hreflangta : 
		https://twitter.com

--------------------------------------------------------


marketing : 
		https://www.binary.net.in

--------------------------------------------------------


hrefhttpstwittercomlangvilink : 
		https://twitter.com

--------------------------------------------------------


nonavbanners : 
		https://twitter.com

--------------------------------------------------------


hreflangth : 
		https://twitter.com

--------------------------------------------------------


to : 
		https://gmpg.org
		https://twitter.com

--------------------------------------------------------


hreflangcs : 
		https://twitter.com

--------------------------------------------------------


story : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlanghilink : 
		https://twitter.com

--------------------------------------------------------


classtextinput : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangtrlink : 
		https://twitter.com

--------------------------------------------------------


classicon : 
		https://twitter.com

--------------------------------------------------------


wordpress : 
		https://api.w.org

--------------------------------------------------------


photoquotquotquotquotsearchquotquotquotquotthis : 
		https://twitter.com

--------------------------------------------------------


idswiftloadingindicator : 
		https://twitter.com

--------------------------------------------------------


emailinput : 
		https://twitter.com

--------------------------------------------------------


twitterp : 
		https://twitter.com

--------------------------------------------------------


downquotquotquotquotload : 
		https://twitter.com

--------------------------------------------------------


loginformpassword : 
		https://twitter.com

--------------------------------------------------------


div : 
		https://twitter.com

--------------------------------------------------------


iconbirdspan : 
		https://twitter.com

--------------------------------------------------------


whats : 
		https://twitter.com

--------------------------------------------------------


sizesany : 
		https://twitter.com

--------------------------------------------------------


content00acedlink : 
		https://twitter.com

--------------------------------------------------------


classloginform : 
		https://twitter.com

--------------------------------------------------------


bird : 
		https://twitter.com

--------------------------------------------------------


relshortcut : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangcslink : 
		https://twitter.com

--------------------------------------------------------


up : 
		https://twitter.com

--------------------------------------------------------


hreflangxdefault : 
		https://twitter.com

--------------------------------------------------------


dirltr : 
		https://twitter.com

--------------------------------------------------------


classnoscriptformbuttoncontainerbutton : 
		https://twitter.com

--------------------------------------------------------


hrefhttpsabstwimgcoma1587147640iconsfaviconsvg : 
		https://twitter.com

--------------------------------------------------------


hreflangnl : 
		https://twitter.com

--------------------------------------------------------


todayh2 : 
		https://twitter.com

--------------------------------------------------------


contentfront : 
		https://twitter.com

--------------------------------------------------------


viewbox0 : 
		https://twitter.com

--------------------------------------------------------


tweetquotquotkquotquotprevious : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangcalink : 
		https://twitter.com

--------------------------------------------------------


engineering : 
		https://www.cumminscollege.org
		https://cumminscollege.org

--------------------------------------------------------


hreflangno : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangsvlink : 
		https://twitter.com

--------------------------------------------------------


classstaticloggedouthomepagesignupsubtitlejoin : 
		https://twitter.com

--------------------------------------------------------


srcijsinstcnameuimetrics : 
		https://twitter.com

--------------------------------------------------------


relmanifest : 
		https://twitter.com

--------------------------------------------------------


titletwitter : 
		https://twitter.com

--------------------------------------------------------


datacomponentlogincallout : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangurlink : 
		https://twitter.com

--------------------------------------------------------


idswiftsectionname : 
		https://twitter.com

--------------------------------------------------------


staticloggedouthomepageutilityblock : 
		https://twitter.com

--------------------------------------------------------


classstaticloggedouthomepageinput : 
		https://twitter.com

--------------------------------------------------------


full : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangdalink : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangbnlink : 
		https://twitter.com

--------------------------------------------------------


submit : 
		https://twitter.com

--------------------------------------------------------


actionhttpstwittercomsessions : 
		https://twitter.com

--------------------------------------------------------


for : 
		https://www.cumminscollege.org
		https://cumminscollege.org

--------------------------------------------------------


h1 : 
		https://twitter.com

--------------------------------------------------------


h2 : 
		https://twitter.com

--------------------------------------------------------


india : 
		https://www.outlookindia.com

--------------------------------------------------------


iconextralargespan : 
		https://twitter.com

--------------------------------------------------------


43514 : 
		https://twitter.com

--------------------------------------------------------


sizes192x192link : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangkn : 
		https://twitter.com

--------------------------------------------------------


version10 : 
		https://twitter.com

--------------------------------------------------------


asyncscriptform : 
		https://twitter.com

--------------------------------------------------------


classstaticloggedouthomepagenosignupform : 
		https://twitter.com

--------------------------------------------------------


typetext : 
		https://twitter.com

--------------------------------------------------------


version11 : 
		https://twitter.com

--------------------------------------------------------


staticloggedouthomepagebuttonlogin : 
		https://twitter.com

--------------------------------------------------------


company : 
		https://www.binary.net.in

--------------------------------------------------------


classstaticloggedouthomepage : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangjalink : 
		https://twitter.com

--------------------------------------------------------


class : 
		https://twitter.com

--------------------------------------------------------


live : 
		https://twitter.com

--------------------------------------------------------


namescribelog : 
		https://twitter.com

--------------------------------------------------------


classstaticloggedouthomepagebuttons : 
		https://twitter.com

--------------------------------------------------------


news : 
		https://twitter.com
		https://www.outlookindia.com

--------------------------------------------------------


relmaskicon : 
		https://twitter.com

--------------------------------------------------------


digital : 
		https://www.binary.net.in

--------------------------------------------------------


hreflangpl : 
		https://twitter.com

--------------------------------------------------------


loginformusername : 
		https://twitter.com

--------------------------------------------------------


hreflangpt : 
		https://twitter.com

--------------------------------------------------------


hreflangfil : 
		https://twitter.com

--------------------------------------------------------


relsearch : 
		https://twitter.com

--------------------------------------------------------


tweet : 
		https://twitter.com

--------------------------------------------------------


home : 
		https://schema.org

--------------------------------------------------------


with : 
		https://twitter.com

--------------------------------------------------------


relappletouchicon : 
		https://twitter.com

--------------------------------------------------------


982 : 
		https://twitter.com

--------------------------------------------------------


form : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlanghrlink : 
		https://twitter.com

--------------------------------------------------------


classnoscriptformlogo : 
		https://twitter.com

--------------------------------------------------------


detected : 
		https://twitter.com

--------------------------------------------------------


developer : 
		https://api.w.org

--------------------------------------------------------


magazine : 
		https://www.outlookindia.com

--------------------------------------------------------


outlook : 
		https://www.outlookindia.com

--------------------------------------------------------


username : 
		https://twitter.com

--------------------------------------------------------


college : 
		https://www.cumminscollege.org
		https://cumminscollege.org

--------------------------------------------------------


edgebuttonsmall : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlanggalink : 
		https://twitter.com

--------------------------------------------------------


typehidden : 
		https://twitter.com

--------------------------------------------------------


staticloggedouthomepage : 
		https://twitter.com

--------------------------------------------------------


hreflangko : 
		https://twitter.com

--------------------------------------------------------


hreflangkn : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangthlink : 
		https://twitter.com

--------------------------------------------------------


twitter : 
		https://twitter.com

--------------------------------------------------------


nameswiftpagesection : 
		https://twitter.com

--------------------------------------------------------


nameuimetrics : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangellink : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlanghulink : 
		https://twitter.com

--------------------------------------------------------


proceed : 
		https://twitter.com

--------------------------------------------------------


jssubmit : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangbglink : 
		https://twitter.com

--------------------------------------------------------


in : 
		https://twitter.com
		https://www.binary.net.in

--------------------------------------------------------


hreflangja : 
		https://twitter.com

--------------------------------------------------------


hreflangzhcn : 
		https://twitter.com

--------------------------------------------------------


contenta : 
		https://twitter.com

--------------------------------------------------------


loggedout : 
		https://twitter.com

--------------------------------------------------------


nonceiiupvgqdlz6kfpwu5tdroa : 
		https://twitter.com

--------------------------------------------------------


is : 
		https://twitter.com

--------------------------------------------------------


edgebuttonmedium : 
		https://twitter.com

--------------------------------------------------------


edgebutton : 
		https://twitter.com

--------------------------------------------------------


script : 
		https://twitter.com

--------------------------------------------------------


hrefopensearchxml : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangkolink : 
		https://twitter.com

--------------------------------------------------------


focusableskip : 
		https://twitter.com

--------------------------------------------------------


hreflangit : 
		https://twitter.com

--------------------------------------------------------


color1da1f2link : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlanggllink : 
		https://twitter.com

--------------------------------------------------------


binary : 
		https://www.binary.net.in

--------------------------------------------------------


hrefhttpstwittercomlangpllink : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangfillink : 
		https://twitter.com

--------------------------------------------------------


women : 
		https://www.cumminscollege.org
		https://cumminscollege.org

--------------------------------------------------------


edgebuttonprimary : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangeslink : 
		https://twitter.com

--------------------------------------------------------


maxwidth : 
		https://twitter.com

--------------------------------------------------------


classthreecol : 
		https://twitter.com

--------------------------------------------------------


classstaticloggedouthomepagesignupheader : 
		https://twitter.com

--------------------------------------------------------


schemaorg : 
		https://schema.org

--------------------------------------------------------


hrefhttpstwittercomlangptlink : 
		https://twitter.com

--------------------------------------------------------


log : 
		https://twitter.com

--------------------------------------------------------


hreflangms : 
		https://twitter.com

--------------------------------------------------------


hreflangmr : 
		https://twitter.com

--------------------------------------------------------


login : 
		https://cumminscollege.truecopy.in

--------------------------------------------------------


hrefhttpstwittercomlangfrlink : 
		https://twitter.com

--------------------------------------------------------


world : 
		https://twitter.com
		https://www.outlookindia.com

--------------------------------------------------------


web : 
		https://www.binary.net.in

--------------------------------------------------------


classuhiddenvisually : 
		https://twitter.com

--------------------------------------------------------


dataatshortcutkeysquotenterquotquotopen : 
		https://twitter.com

--------------------------------------------------------


get : 
		https://twitter.com

--------------------------------------------------------


tweetsquotquotguquotquotgo : 
		https://twitter.com

--------------------------------------------------------


namesessionusernameoremail : 
		https://twitter.com

--------------------------------------------------------


hreflangzhtw : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangtalink : 
		https://twitter.com

--------------------------------------------------------


classstaticloggedouthomepagecontent : 
		https://twitter.com

--------------------------------------------------------


contentnoodp : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangmrlink : 
		https://twitter.com

--------------------------------------------------------


xmlnshttpwwww3org2000svg : 
		https://twitter.com

--------------------------------------------------------


relnoopenerforgot : 
		https://twitter.com

--------------------------------------------------------


autocompleteoff : 
		https://twitter.com

--------------------------------------------------------


classstaticloggedouthomepagesignuptitlesee : 
		https://twitter.com

--------------------------------------------------------


typeapplicationopensearchdescription+xml : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangsrlink : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangnllink : 
		https://twitter.com

--------------------------------------------------------


gmpg : 
		https://gmpg.org

--------------------------------------------------------


hrefhttpstwittercomlangrolink : 
		https://twitter.com

--------------------------------------------------------


opinion : 
		https://www.outlookindia.com

--------------------------------------------------------


hrefabstwimgcomfaviconsfaviconico : 
		https://twitter.com

--------------------------------------------------------


contentfrom : 
		https://twitter.com

--------------------------------------------------------


hrefhttpsmobiletwittercom : 
		https://twitter.com

--------------------------------------------------------


edgebuttonsecondary : 
		https://twitter.com

--------------------------------------------------------


dataelementform : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangidlink : 
		https://twitter.com

--------------------------------------------------------


staticloggedouthomepagenarrowloginbutton : 
		https://twitter.com

--------------------------------------------------------


span : 
		https://twitter.com

--------------------------------------------------------


what39s : 
		https://twitter.com

--------------------------------------------------------


hreflangga : 
		https://twitter.com

--------------------------------------------------------


autocompletecurrentpassword : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangengblink : 
		https://twitter.com

--------------------------------------------------------


ufloatright : 
		https://twitter.com

--------------------------------------------------------


sign : 
		https://twitter.com

--------------------------------------------------------


hreflanggl : 
		https://twitter.com

--------------------------------------------------------


screen : 
		https://twitter.com

--------------------------------------------------------


passworda : 
		https://twitter.com

--------------------------------------------------------


body : 
		https://twitter.com

--------------------------------------------------------


commentarymeta : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercom : 
		https://twitter.com

--------------------------------------------------------


tweetquotquotspacequotquotpage : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangzhtwlink : 
		https://twitter.com

--------------------------------------------------------


classloginformstaticforgot : 
		https://twitter.com

--------------------------------------------------------


classjsnav : 
		https://twitter.com

--------------------------------------------------------


namerobots : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangmslink : 
		https://twitter.com

--------------------------------------------------------


that : 
		https://twitter.com

--------------------------------------------------------


hreflangfr : 
		https://twitter.com

--------------------------------------------------------


placeholderpassword : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangsklink : 
		https://twitter.com

--------------------------------------------------------


typepassword : 
		https://twitter.com

--------------------------------------------------------


only : 
		https://twitter.com

--------------------------------------------------------


documentbodyclassnamedocumentbodyclassname+ : 
		https://twitter.com

--------------------------------------------------------


menuquotquotjquotquotnext : 
		https://twitter.com

--------------------------------------------------------


useru2026quot : 
		https://twitter.com

--------------------------------------------------------


all : 
		https://twitter.com

--------------------------------------------------------


placeholderphone : 
		https://twitter.com

--------------------------------------------------------


new : 
		https://twitter.com

--------------------------------------------------------


actionhttpsmobiletwittercominojsrouterpath2f : 
		https://twitter.com

--------------------------------------------------------


hreflangfa : 
		https://twitter.com

--------------------------------------------------------


like : 
		https://twitter.com

--------------------------------------------------------


value1e8fed4c62cdebdbf20957895adf5ba681d3ae0c : 
		https://twitter.com

--------------------------------------------------------


hreflangfi : 
		https://twitter.com

--------------------------------------------------------


640px : 
		https://twitter.com

--------------------------------------------------------


iconlogo : 
		https://twitter.com

--------------------------------------------------------


hreflanges : 
		https://twitter.com

--------------------------------------------------------


hrefaccountbeginpasswordreset : 
		https://twitter.com

--------------------------------------------------------


hrefmanifestjson : 
		https://twitter.com

--------------------------------------------------------


hreflangen : 
		https://twitter.com

--------------------------------------------------------


noscript : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangfilink : 
		https://twitter.com

--------------------------------------------------------


hreflangel : 
		https://twitter.com

--------------------------------------------------------


hreflangvi : 
		https://twitter.com

--------------------------------------------------------


hrefhttpstwittercomlangnolink : 
		https://twitter.com

--------------------------------------------------------


hreflangeu : 
		https://twitter.com

--------------------------------------------------------


youtube : 
		https://www.youtube.com

--------------------------------------------------------


politics : 
		https://twitter.com

--------------------------------------------------------


valuelog : 
		https://twitter.com

--------------------------------------------------------


handbook : 
		https://api.w.org

--------------------------------------------------------


hreflangid : 
		https://twitter.com

--------------------------------------------------------


legacy : 
		https://twitter.com

--------------------------------------------------------


link : 
		https://twitter.com

--------------------------------------------------------


generator : 
		https://twitter.com

--------------------------------------------------------


relcanonical : 
		https://twitter.com

--------------------------------------------------------


iddoc : 
		https://twitter.com

--------------------------------------------------------


detailsquotquotoquotquotexpand : 
		https://twitter.com

--------------------------------------------------------


hrefhttpsabstwimgcomiconsappletouchicon192x192png : 
		https://twitter.com

--------------------------------------------------------


nameauthenticitytoken : 
		https://twitter.com

--------------------------------------------------------


head : 
		https://twitter.com

--------------------------------------------------------


hreflanghu : 
		https://twitter.com

--------------------------------------------------------


452 : 
		https://twitter.com

--------------------------------------------------------


typeimagexiconlink : 
		https://twitter.com

--------------------------------------------------------


hreflanghr : 
		https://twitter.com

--------------------------------------------------------


classforgot : 
		https://twitter.com

--------------------------------------------------------


and : 
		https://twitter.com

--------------------------------------------------------


namereturntossl : 
		https://twitter.com

--------------------------------------------------------


of : 
		https://www.cumminscollege.org
		https://cumminscollege.org

--------------------------------------------------------


browser : 
		https://twitter.com

--------------------------------------------------------


today : 
		https://www.outlookindia.com

--------------------------------------------------------


value : 
		https://twitter.com

--------------------------------------------------------


classtwittericonbird : 
		https://twitter.com

--------------------------------------------------------


on : 
		https://www.outlookindia.com

--------------------------------------------------------


rest : 
		https://api.w.org

--------------------------------------------------------


hreflanghe : 
		https://twitter.com

--------------------------------------------------------


edgebuttonprimaryyesbuttonp : 
		https://twitter.com

--------------------------------------------------------


or : 
		https://twitter.com

--------------------------------------------------------


sports : 
		https://twitter.com
		https://www.outlookindia.com

--------------------------------------------------------


idasynccssplaceholder : 
		https://twitter.com

--------------------------------------------------------


namesessionpassword : 
		https://twitter.com

--------------------------------------------------------


resources : 
		https://api.w.org

--------------------------------------------------------


hreflanghi : 
		https://twitter.com

--------------------------------------------------------


hreflanggu : 
		https://twitter.com

--------------------------------------------------------


truecopy : 
		https://cumminscollege.truecopy.in

--------------------------------------------------------


methodpost : 
		https://twitter.com

--------------------------------------------------------


nameswiftpagename : 
		https://twitter.com

--------------------------------------------------------


jsfrontsignin : 
		https://twitter.com

--------------------------------------------------------


namedescription : 
		https://twitter.com

--------------------------------------------------------


sketch : 
		https://twitter.com

--------------------------------------------------------


1208 : 
		https://twitter.com

--------------------------------------------------------


cummins : 
		https://www.cumminscollege.org
		https://cumminscollege.org

--------------------------------------------------------


 */