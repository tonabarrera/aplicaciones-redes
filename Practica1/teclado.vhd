library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;
entity teclado is
    port (
        clk, clr: in std_logic;
        f:in std_logic_vector(3 downto 0);
        c:inout std_logic_vector(2 downto 0);
        display: out std_logic_vector(6 downto 0)
    );
end entity;
architecture arq_teclado of teclado is
    signal tecla: std_logic_vector(6 downto 0);
    constant DIG0:std_logic_vector(6 downto 0) := "0000001";
    constant DIG1:std_logic_vector(6 downto 0) := "1001111";
    constant DIG2:std_logic_vector(6 downto 0) := "0010010";
    constant DIG3:std_logic_vector(6 downto 0) := "0000110";
    constant DIG4:std_logic_vector(6 downto 0) := "1001100";
    constant DIG5:std_logic_vector(6 downto 0) := "0100100";
    constant DIG6:std_logic_vector(6 downto 0) := "0100000";
    constant DIG7:std_logic_vector(6 downto 0) := "0001111";
    constant DIG8:std_logic_vector(6 downto 0) := "0000000";
    constant DIG9:std_logic_vector(6 downto 0) := "0000100";
    constant DIGA:std_logic_vector(6 downto 0) := "0001000";
    constant DIGH:std_logic_vector(6 downto 0) := "1001000";
    constant APAG: std_logic_vector(6 downto 0) := "1111111";
    begin

	anillo:process(clk, clr)
        begin
        if(clr = '1') then
            c<="110";
        elsif (clk'event and clk='1') then
            c<= to_stdlogicvector(to_bitvector(c) ror 1);
        end if;
    end process;

    deco:process(f, c)
        begin
        case f&c is
            when "1110" & "011" => tecla <= DIG1;
            when "1110" & "101" => tecla <= DIG2;
            when "1110" & "110" => tecla <= DIG3;
            when "1101" & "011" => tecla <= DIG4;
            when "1101" & "101" => tecla <= DIG5;
            when "1101" & "110" => tecla <= DIG6;
            when "1011" & "011" => tecla <= DIG7;
            when "1011" & "101" => tecla <= DIG8;
            when "1011" & "110" => tecla <= DIG9;
            when "0111" & "011" => tecla <= DIGA;
            when "0111" & "101" => tecla <= DIG0;
            when "0111" & "110" => tecla <= DIGH;
            when others => tecla <= APAG;
        end case;
    end process;
		
    registro:process(clk, clr, tecla)
       begin
        if(clr='1') then
           display <= APAG;
        elsif (clk'event and clk='1') then
           if(f="1111") then
                display<=display;
            else
               display<=tecla;
            end if;
        end if;
    end process;
end architecture;
