package com.examples.android.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;



/** Utilizado para criar todos os textos que envolvem calculos de data para exibir na Agenda GT. */
public class TextoDataCalendario {

	
	
	public String textoOpcaoMenuVisaoCalendarioSemanaCorrente;
	public String textoOpcaoMenuVisaoCalendarioDiaEMes;
	public String textoOpcaoMenuVisaoCalendarioMes;
	
	public String textoLinkMenuVisaoCalendarioDiario;
	public String textoLinkMenuVisaoCalendarioMensal;
	
	private GregorianCalendar data;

	private SimpleDateFormat simpleDateFormat;
	private String dataFormatada;


	
	
	/** Construtor. */
	public TextoDataCalendario ( GregorianCalendar mes, GregorianCalendar dia  )
	{
		this.simpleDateFormat = new SimpleDateFormat( "dd/MM/yyyy", new Locale( "pt", "BR" ) );
		
		this.executarCriacaoTextos( mes, dia );
	}


	
	/** Utilizado para encapsular a execução de criaçao de todos os textos a partir das novas datas. */
	public void executarCriacaoTextos( GregorianCalendar mes, GregorianCalendar dia )
	{
		if ( mes != null )
		{
			this.data = mes;
			this.dataFormatada = this.simpleDateFormat.format( this.data.getTime() );
			criarTextoLinkMenuVisaoCalendarioMensal();
		}
		else if ( dia != null )
		{
			this.data = dia;
			this.dataFormatada = this.simpleDateFormat.format( this.data.getTime() );
			criarTextoLinkMenuVisaoCalendarioDiario();
		}
		
		criarTextoOpcaoMenuVisaoCalendarioSemanaCorrente();
		criarTextoOpcaoMenuVisaoCalendarioDiaEMes();
		criarTextoOpcaoMenuVisaoCalendarioMes();
		
	}
	
	

	/** Utilizado para criar o texto da semana corrente. Esse texto fica na opção semana do menu de visão do calendário. */
	private void criarTextoOpcaoMenuVisaoCalendarioSemanaCorrente()
	{
		GregorianCalendar gregorianCalendarSemanaCorrente = (GregorianCalendar) this.data.clone();
		gregorianCalendarSemanaCorrente.set( Calendar.HOUR_OF_DAY, 0 );
		gregorianCalendarSemanaCorrente.clear( Calendar.MINUTE );
		gregorianCalendarSemanaCorrente.clear( Calendar.SECOND );
		gregorianCalendarSemanaCorrente.clear( Calendar.MILLISECOND );

		gregorianCalendarSemanaCorrente.set( Calendar.DAY_OF_WEEK, gregorianCalendarSemanaCorrente.getFirstDayOfWeek() ); 
		
		Integer mesPrimeiroDiaSemana = gregorianCalendarSemanaCorrente.get( Calendar.MONTH );
		String  mesPrimeiroDiaSemanaPorExtenso = android.text.format.DateFormat.format( "MMMM", gregorianCalendarSemanaCorrente ).toString();
		
		
		String primeiroDiaSemanaCorrenteString = simpleDateFormat.format( gregorianCalendarSemanaCorrente.getTime() );
		
		// Set the calendar to monday of the current week
		gregorianCalendarSemanaCorrente.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
		String ultimoDiaSemanaCorrenteString = simpleDateFormat.format( gregorianCalendarSemanaCorrente.getTime() );
		
		Integer mesUltimoDiaSemana = gregorianCalendarSemanaCorrente.get( Calendar.MONTH );
		String mesUltimoDiaSemanaPorExtenso = android.text.format.DateFormat.format( "MMMM", gregorianCalendarSemanaCorrente ).toString();
		
		if ( mesPrimeiroDiaSemana == mesUltimoDiaSemana )
		{
			this.textoOpcaoMenuVisaoCalendarioSemanaCorrente = primeiroDiaSemanaCorrenteString.substring( 0, 2 ) + " - " + 
															   ultimoDiaSemanaCorrenteString.substring( 0, 2 ) + " de " + 
															   mesPrimeiroDiaSemanaPorExtenso;
		}
		else
		{
			this.textoOpcaoMenuVisaoCalendarioSemanaCorrente = primeiroDiaSemanaCorrenteString.substring( 0, 2 ) + " de " + mesPrimeiroDiaSemanaPorExtenso.substring( 0, 3 ) + " - " + 
								  							   ultimoDiaSemanaCorrenteString.substring( 0, 2 )  + " de " + mesUltimoDiaSemanaPorExtenso.substring( 0, 3 );
		}
	}
	
	
	
	/** 1 de maio de 2014 */
	private void criarTextoLinkMenuVisaoCalendarioDiario(  )
	{
		this.textoLinkMenuVisaoCalendarioDiario = dataFormatada.substring( 0 , 2 ) + " de " + 
												  android.text.format.DateFormat.format( "MMMM", data ).toString() + " de " +
												  dataFormatada.substring( 6 , 10 );
												  
	}
	
	

	/** maio de 2014 */
	private void criarTextoLinkMenuVisaoCalendarioMensal(  )
	{
		this.textoLinkMenuVisaoCalendarioMensal = android.text.format.DateFormat.format( "MMMM", data ).toString() + " de " +
												  dataFormatada.substring( 6 , 10 );
	}

	
	
	/** 1 de maio */
	private void criarTextoOpcaoMenuVisaoCalendarioDiaEMes(  )
	{
		this.textoOpcaoMenuVisaoCalendarioDiaEMes =	dataFormatada.substring( 0 , 2 ) + " de " + 
				  									android.text.format.DateFormat.format( "MMMM", data ).toString();
	}


	
	/** maio */
	private void criarTextoOpcaoMenuVisaoCalendarioMes( )
	{
		this.textoOpcaoMenuVisaoCalendarioMes = android.text.format.DateFormat.format( "MMMM", data ).toString();
	}
	
}
