library	ieee;
use	library ieee.std_logic_1164.all;


entity PRAC8 is port (
	filas 		: in	std_logic_vector(2 downto 0);
	columnas 	: inout std_logic_vector(2 downto 0);
	CLK,CLR		: in std_logic;
	DISPLAY 	: inout std_logic_vector(6 downto 0);
  ) ;
end entity ; --PRAC8 

architecture APRAC8 of PRAC8 is

signal tecla : std_logic_vector(6 downto 0);
constant DIG0: std_logic_vector(6 downto0):="0000001";
constant DIG1: std_logic_vector(6 downto0):="1001111";
constant DIG2: std_logic_vector(6 downto0):="0010010";
constant DIG3: std_logic_vector(6 downto0):="0000110";
constant DIG4: std_logic_vector(6 downto0):="1001100";
constant DIG5: std_logic_vector(6 downto0):="0100100";
constant DIG6: std_logic_vector(6 downto0):="0100000";
constant DIG7: std_logic_vector(6 downto0):="0001111";
constant DIG8: std_logic_vector(6 downto0):="0000000";
constant DIG9: std_logic_vector(6 downto0):="0001100";
constant ASTE: std_logic_vector(6 downto0):="1111110";
constant GATO: std_logic_vector(6 downto0):="1001000";
constant NOTTECLA: std_logic_vector(6 downto0):="1111111";

begin

process(filas,columnas)
	begin
	case( filas & columnas ) is
	
		when "0111"&"011" => tecla <=DIG1;
		when "0111"&"101" => tecla <=DIG2;
		when "0111"&"110" => tecla <=DIG3;
		when "1011"&"011" => tecla <=DIG4;
		when "1011"&"101" => tecla <=DIG5;
		when "1011"&"110" => tecla <=DIG6;
		when "1101"&"011" => tecla <=DIG7;
		when "1101"&"101" => tecla <=DIG8;
		when "1101"&"110" => tecla <=DIG9;
		when "1110"&"011" => tecla <=ASTE;
		when "1110"&"101" => tecla <=DIG0;
		when "1110"&"110" => tecla <=GATO;
		when others => tecla <= NOTTECLA;
	end case ;
end process;

ANILLO: process(CLR,CLK)
	begin
	if(CLR = '0') then
		columnas <= "110"; --Mandarlo al inicio de alguna de las posiciones de tierra
	elsif (CLK'EVENT AND CLK='1') then
	columnas <= to_stdlogicvector(to_bitvector(columnas)ROR 1);
		--Guardando en c el corrimiento a la derecha de una posicion 
	end if;
end ANILLO;

REGISTRO(CLR,CLK,DISPLAY)
	begin
	if (CLR='0') then
		DISPLAY <= NOTTECLA;
	elsif (CLK'EVENT AND CLK = '1') then
		if (filas="111") then
			DISPLAY <= DISPLAY 	--En el caso donde no se presiono ninguna tecla
		else
			DISPLAY<=tecla; 	-- Refrescar si existe algun dato presionado
		end if ;
	end if ;
end REGISTRO;
end APRAC8 ; -- APRAC8