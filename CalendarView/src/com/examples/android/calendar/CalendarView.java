package com.examples.android.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class CalendarView extends Activity 
{

	/** Instancias Calendar. */
	public GregorianCalendar mesSelecionado; 
	public GregorianCalendar diaSelecionado;
	
	public GregorianCalendar itemmonth;
	
	public String diaEMesPorExtenso;
	public String mesPorExtenso;
	public String mesEAnoPorExtenso;
	public String textoLinkMenuVisaoCalendario;
	
	public Integer diaDoMes;

	public Boolean exibirVisaoCalendarioDiaria;
	public Boolean exibirVisaoCalendarioSemanal;
	public Boolean exibirVisaoCalendarioMensal;
	public Boolean exibirVisaoCalendarioCompromissos;
	
	/** Instancia CalendarAdapter. */
	public CalendarAdapter calendarAdapter; 
	
	/** Para pegar alguns valores de evento de para mostrar o marcador de ponto. */
	public Handler handler;
	
	/** Recipiente para armazenar itens de calendário que precisa mostrar o marcador de eventos. */
	public ArrayList<String> items; 

	ArrayList<String> event;
	LinearLayout rLayout;
	ArrayList<String> date;
	ArrayList<String> desc;

	private TextoDataCalendario textoDataCalendario;
	
	
	
	
	
	
	/** */
	public void onCreate( Bundle savedInstanceState  ) 
	{
		
		super.onCreate( savedInstanceState );
		setContentView( R.layout.calendar );
		
		inicializarAtributosDeData();
		
		criarMenuVisaoCalendario();
		
		/*
		RelativeLayout relativeLayoutPrevious = (RelativeLayout) findViewById( R.id.previous );
		relativeLayoutPrevious.setOnClickListener( 
		new OnClickListener() 
		{
			@Override
			public void onClick( View v ) 
			{
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout relativeLayoutNext = (RelativeLayout) findViewById( R.id.next );
		relativeLayoutNext.setOnClickListener( 
		new OnClickListener() 
		{
			@Override
			public void onClick( View v ) 
			{
				setNextMonth();
				refreshCalendar();
			}
		});

		*/
		
		this.items = new ArrayList<String>();
		
		this.rLayout = (LinearLayout) findViewById( R.id.text );
		
		this.calendarAdapter = new CalendarAdapter( this, mesSelecionado );

		GridView gridview = (GridView) findViewById( R.id.gridview );
		gridview.setAdapter( calendarAdapter );

		this.handler = new Handler();
		this.handler.post( calendarUpdater );
		
		gridview.setOnItemClickListener(
		new OnItemClickListener() 
		{
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) 
			{
				/** Removendo o ponto de vista anterior, se adicionado. */
				if ( rLayout.getChildCount() > 0 ) 
				{
					rLayout.removeAllViews();
				}
				
				desc = new ArrayList<String>();
				date = new ArrayList<String>();
				
				CalendarAdapter calendarAdapter = (CalendarAdapter) parent.getAdapter();
				calendarAdapter.setSelected( view );
				
				String selectedGridDate = CalendarAdapter.dayString.get( position );
				String[] separatedTime = selectedGridDate.split( "/" );
				
				/** Tomando última parte da data. Ou seja, 2 a partir de 2012-12-02. */
 				String gridvalueString = separatedTime[ 2 ].replaceFirst( "^0*", "" );
				int gridvalue = Integer.parseInt( gridvalueString );
				
				/** Navigate to next or previous month on clicking offdays. */
				if ( gridvalue > 10 && position < 8 ) 
				{
					setPreviousMonth();
					refreshCalendar();
				} 
				else if ( gridvalue < 7 && position > 28 ) 
				{
					setNextMonth();
					refreshCalendar();
				}
				
				calendarAdapter.setSelected( view );

				for ( int i = 0; i < Utility.startDates.size(); i++ )
				{
					if ( Utility.startDates.get( i ).equals( selectedGridDate ) ) 
					{
						desc.add( Utility.nameOfEvent.get( i ) );
					}
				}

				if ( desc.size() > 0 ) 
				{
					for ( int i = 0; i < desc.size(); i++ ) 
					{
						TextView rowTextView = new TextView( CalendarView.this );

						/** Defina algumas propriedades de rowTextView ou algo. */
						rowTextView.setText( "Event:" + desc.get( i ) );
						rowTextView.setTextColor( Color.BLACK );

						// add the textview to the linearlayout
						rLayout.addView( rowTextView );
					}
				}

				desc = null;
			}
		});
	}
	
	
	
	/** Utilizado para inicializar os atributos de data. */
	private void inicializarAtributosDeData()
	{
		Locale.setDefault( new Locale( "pt", "BR" ) );  
		GregorianCalendar dataAtual = (GregorianCalendar) GregorianCalendar.getInstance();
		
		this.mesSelecionado = (GregorianCalendar) dataAtual.clone();
		this.itemmonth = (GregorianCalendar) dataAtual.clone();
		
		this.textoDataCalendario = new TextoDataCalendario( mesSelecionado, null );
		this.textoLinkMenuVisaoCalendario = this.textoDataCalendario.textoLinkMenuVisaoCalendarioMensal;
		
	}


	
	/** Utilizado para criar o menu de visao do calendario*/
	private void criarMenuVisaoCalendario()
	{
		final String[] option = new String[] { "Dia				                        " + this.textoDataCalendario.textoOpcaoMenuVisaoCalendarioDiaEMes, 
			       							   "Semana                                        " + this.textoDataCalendario.textoOpcaoMenuVisaoCalendarioSemanaCorrente, 
			       							   "Mês                                                 " + this.textoDataCalendario.textoOpcaoMenuVisaoCalendarioMes, 
			       							   "Compromissos                          " + this.textoDataCalendario.textoOpcaoMenuVisaoCalendarioDiaEMes }; 
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.selecao_visao_calendario, option); 
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( this, R.style.Dialog ); 

		alertDialogBuilder.setAdapter( arrayAdapter, 
		new DialogInterface.OnClickListener() 
		{
			public void onClick( DialogInterface dialog, int which ) 
			{ 
				switch( which )  
				{
					/** Diaria */
					case 0:
						textoLinkMenuVisaoCalendario = textoDataCalendario.textoLinkMenuVisaoCalendarioDiario;
						exibirVisaoCalendarioDiaria = true;
						exibirVisaoCalendarioSemanal = false;
						exibirVisaoCalendarioMensal = false;
						exibirVisaoCalendarioCompromissos = false;
						break;
					
					/** Semanal */
					case 1:
						textoLinkMenuVisaoCalendario = textoDataCalendario.textoLinkMenuVisaoCalendarioMensal;
						exibirVisaoCalendarioSemanal = true;
						exibirVisaoCalendarioDiaria = false;
						exibirVisaoCalendarioMensal = false;
						exibirVisaoCalendarioCompromissos = false;
						break;
					
					/** Mensal */
					case 2: 
						textoLinkMenuVisaoCalendario = textoDataCalendario.textoLinkMenuVisaoCalendarioMensal;
						exibirVisaoCalendarioMensal = true;
						exibirVisaoCalendarioSemanal = false;
						exibirVisaoCalendarioDiaria = false;
						exibirVisaoCalendarioCompromissos = false;
						break;
					
					/** Compromissos */
					case 3:
						textoLinkMenuVisaoCalendario = textoDataCalendario.textoLinkMenuVisaoCalendarioDiario;
						exibirVisaoCalendarioCompromissos = true;
						exibirVisaoCalendarioMensal = false;
						exibirVisaoCalendarioSemanal = false;
						exibirVisaoCalendarioDiaria = false;
						break;
				} 
			}
		}); 

		final AlertDialog alertDialog = alertDialogBuilder.create(); 
		alertDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );

		TextView textViewVisaoCalendario = (TextView) findViewById( R.id.textoVisaoCalendario ); 
		textViewVisaoCalendario.setText( this.textoLinkMenuVisaoCalendario );

		textViewVisaoCalendario.setOnClickListener(
		new View.OnClickListener() 
		{ 
			public void onClick( View v ) 
			{
				Window window = alertDialog.getWindow();
				window.setLayout( 350, 300 );

				WindowManager.LayoutParams layoutParams = window.getAttributes();
				layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
				layoutParams.x = 50;   
				layoutParams.y = 128;  

				alertDialog.show();
			}
		} );
	}
	
	
	
	
	/** */
	protected void setNextMonth() 
	{
		if ( mesSelecionado.get( GregorianCalendar.MONTH ) == mesSelecionado.getActualMaximum( GregorianCalendar.MONTH ) ) 
		{
			mesSelecionado.set( mesSelecionado.get( GregorianCalendar.YEAR ) + 1, mesSelecionado.getActualMinimum( GregorianCalendar.MONTH ), 1 );
		} 
		else 
		{
			mesSelecionado.set( GregorianCalendar.MONTH, mesSelecionado.get( GregorianCalendar.MONTH ) + 1 );
		}
	}
	
	

	/** */
	protected void setPreviousMonth() 
	{
		if ( mesSelecionado.get( GregorianCalendar.MONTH ) == mesSelecionado.getActualMinimum( GregorianCalendar.MONTH ) ) 
		{
			mesSelecionado.set( mesSelecionado.get( GregorianCalendar.YEAR ) - 1, mesSelecionado.getActualMaximum( GregorianCalendar.MONTH ), 1 );
		} 
		else 
		{
			mesSelecionado.set( GregorianCalendar.MONTH, mesSelecionado.get( GregorianCalendar.MONTH ) - 1 );
		}
	}

	
	
	/** */
	protected void showToast( String string ) 
	{
		Toast.makeText( this, string, Toast.LENGTH_SHORT ).show();
	}

	
	
	/** Atualizar Dados do Calendario */
	public void refreshCalendar() 
	{
		calendarAdapter.refreshDays();
		calendarAdapter.notifyDataSetChanged();
		
		
		/** Gerar alguns itens de calendário */
		handler.post( calendarUpdater ); 
		
		this.textoDataCalendario.executarCriacaoTextos( mesSelecionado, null );

		TextView textoVisaoCalendario = (TextView) findViewById( R.id.textoVisaoCalendario );
		textoVisaoCalendario.setText( this.textoDataCalendario.textoLinkMenuVisaoCalendarioMensal );
	}
	
	

	/** */
	public Runnable calendarUpdater = new Runnable() 
	{
		@Override
		public void run() 
		{
			items.clear();

			/** As datas são impressas da semana atual */
			DateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy", new Locale("pt", "BR") );
			
			String itemvalue;
			
			event = Utility.readCalendarEvent( CalendarView.this );
			
			Log.d( "=====Event====", event.toString() );
			Log.d( "=====Date ARRAY====", Utility.startDates.toString() );

			for ( int i = 0; i < Utility.startDates.size(); i++ ) 
			{
				itemvalue = dateFormat.format( itemmonth.getTime() );
				Log.d( "=====itemvalue====", itemvalue );
				itemmonth.add( GregorianCalendar.DATE, 1 );
				items.add( Utility.startDates.get(i).toString() );
			}
			
			calendarAdapter.setItems( items );
			calendarAdapter.notifyDataSetChanged();
		}
	};
	
	
	
	
	
	
	
}
