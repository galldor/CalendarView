package com.examples.android.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter 
{
	/** Exemplo de calend�rio para o m�s anterior. */
	public GregorianCalendar pmonth; 
	
	/** Calend�rio inst�ncia para m�s anterior para obter vis�o completa. */
	public GregorianCalendar pmonthmaxset;
	
	public static List<String> dayString;
	
	private Context mContext;
	
	private Calendar month;
	
	private GregorianCalendar selectedDate;
	
	int firstDay;
	int maxWeeknumber;
	int maxP;
	int calMaxP;
	int lastWeekDay;
	int leftDays;
	int mnthlength;
	String itemvalue, curentDateString;
	DateFormat dateFormat;

	private ArrayList<String> items;
	
	private View previousView;

	
	
	/** Construtor */
	public CalendarAdapter( Context c, GregorianCalendar monthCalendar ) 
	{
		CalendarAdapter.dayString = new ArrayList<String>();
		Locale.setDefault( new Locale("pt", "BR") );
		
		month = monthCalendar;
		selectedDate = (GregorianCalendar) monthCalendar.clone();
		mContext = c;
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);
		
		this.items = new ArrayList<String>();
		dateFormat = new SimpleDateFormat( "dd/MM/yyyy", new Locale("pt", "BR") );
		curentDateString = dateFormat.format(selectedDate.getTime());
		refreshDays();
	}

	
	
	/**  */
	public void setItems( ArrayList<String> items ) 
	{
		for ( int i = 0; i != items.size(); i++ ) 
		{
			if ( items.get( i ).length() == 1 ) 
			{
				items.set( i, "0" + items.get( i ) );
			}
		}
		this.items = items;
	}

	
	
	/** */
	public int getCount() 
	{
		return dayString.size();
	}

	
	
	/** */
	public Object getItem( int position ) 
	{
		return dayString.get( position );
	}

	
	
	/** */
	public long getItemId( int position ) 
	{
		return 0;
	}

	
	
	/** Criar uma nova vis�o para cada item referenciado pelo adaptador */
	public View getView( int position, View convertView, ViewGroup parent ) 
	{
		View view = convertView;
		
		/** Se n�o for reciclado, inicializar alguns atributos */
		if ( convertView == null ) 
		{ 
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			view = layoutInflater.inflate( R.layout.calendar_item, null );
		}
		 
		TextView dayTextView = (TextView) view.findViewById( R.id.date );
		
		/** Separa daystring em partes */
		String[] separatedTime = dayString.get( position ).split( "/" );
		
		/** Tomando �ltima parte do encontro. ie; 2 a partir de 2012-12-02 */
		String gridvalue = separatedTime[0].replaceFirst( "^0*", "" );
		
		/** Verificar se o dia � no m�s atual ou n�o. */
		if ( Integer.parseInt( gridvalue ) > 1 && position < firstDay ) 
		{
			/** Definindo offdays a cor branca. */
			dayTextView.setTextColor( Color.WHITE );
			dayTextView.setClickable( false );
			dayTextView.setFocusable( false );
		} 
		else if ( Integer.parseInt( gridvalue ) < 7 && position > 28 ) 
		{
			dayTextView.setTextColor( Color.WHITE );
			dayTextView.setClickable( false );
			dayTextView.setFocusable( false );
		} 
		else 
		{
			/** Configura��o dias do m�s curent na cor azul. */
			dayTextView.setTextColor( Color.BLUE );
		}

		if ( dayString.get( position ).equals( curentDateString ) ) 
		{
			setSelected( view );
			previousView = view;
		} 
		else 
		{
			view.setBackgroundResource( R.drawable.list_item_background );
		}
		
		dayTextView.setText( gridvalue );

		/** Criar cadeia de data para compara��o */
		String date = dayString.get( position );

		if ( date.length() == 1 ) 
		{
			date = "0" + date;
		}
		
		String monthStr = "" + month.get( GregorianCalendar.MONTH ) + 1 ;
		if ( monthStr.length() == 1 ) 
		{
			monthStr = "0" + monthStr;
		}

		/** �cone mostrar se a data n�o est� vazio e ela existe na matriz itens */
		ImageView imageView = (ImageView) view.findViewById( R.id.date_icon );
		if ( date.length() > 0 && items != null && items.contains( date ) )
		{
			imageView.setVisibility( View.VISIBLE );
		} 
		else 
		{
			imageView.setVisibility( View.INVISIBLE );
		}
		
		return view;
	}

	
	
	/** */
	public View setSelected( View view ) 
	{
		if ( previousView != null ) 
		{
			previousView.setBackgroundResource( R.drawable.list_item_background );
		}
		
		previousView = view;
		view.setBackgroundResource( R.drawable.calendar_cel_selectl );
		
		return view;
	}

	
	
	/** */
	public void refreshDays() 
	{
		/** Limpa os Itens */
		items.clear();
		dayString.clear();
		
		Locale.setDefault( new Locale( "pt", "BR" ) );  
		pmonth = (GregorianCalendar) month.clone();
		
		/** M�s de in�cio day. ie; sun, mon, etc */
		firstDay = month.get( GregorianCalendar.DAY_OF_WEEK );
		
		/** Encontrar o n�mero de semanas no m�s atual. */
		maxWeeknumber = month.getActualMaximum( GregorianCalendar.WEEK_OF_MONTH );
		
		/** Alocar n�mero m�ximo de linhas para o gridview. */
		mnthlength = maxWeeknumber * 7;
		
		/** Dia anterior m�s, no m�ximo 31,30 */
		maxP = getMaxP(); 
		
		/** Calend�rio offday partida 24,25 ... */
		calMaxP = maxP - (firstDay - 1);
		
		/** Inst�ncia Calend�rio para a obten��o de um gridview completo, incluindo (anterior, atual, pr�xima) a tr�s do m�s. */
		pmonthmaxset = (GregorianCalendar) pmonth.clone();
		
		/** Definir a data de in�cio como data exigida do m�s anterior. */
		pmonthmaxset.set( GregorianCalendar.DAY_OF_MONTH, calMaxP + 1 );

		
		/** filling calendar gridview. */
		for ( int n = 0; n < mnthlength; n++ ) 
		{
			itemvalue = dateFormat.format( pmonthmaxset.getTime() );
			pmonthmaxset.add( GregorianCalendar.DATE, 1 );
			dayString.add( itemvalue );
		}
	}

	
	
	/** */
	private int getMaxP() 
	{
		int maxP;
		if ( month.get( GregorianCalendar.MONTH) == month.getActualMinimum( GregorianCalendar.MONTH ) ) 
		{
			pmonth.set( ( month.get( GregorianCalendar.YEAR ) - 1), month.getActualMaximum( GregorianCalendar.MONTH ), 1 );
		} 
		else 
		{
			pmonth.set( GregorianCalendar.MONTH, month.get( GregorianCalendar.MONTH ) - 1 );
		}
		maxP = pmonth.getActualMaximum( GregorianCalendar.DAY_OF_MONTH );

		return maxP;
	}

}