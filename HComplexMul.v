// Generator : SpinalHDL v1.4.1    git head : 99d6d471af204b6d7d9f63fae58757e9d3c7b944
// Component : HComplexMul



module HComplexMul (
  input      [15:0]   a_real,
  input      [15:0]   a_imag,
  input      [15:0]   b_real,
  input      [15:0]   b_imag,
  output     [15:0]   c_real,
  output     [15:0]   c_imag
);
  wire       [31:0]   _zz_3;
  wire       [31:0]   _zz_4;
  wire       [31:0]   _zz_5;
  wire       [15:0]   fixTo_dout;
  wire       [15:0]   fixTo_1_dout;
  wire       [15:0]   fixTo_2_dout;
  wire       [15:0]   _zz_6;
  wire       [15:0]   _zz_7;
  wire       [15:0]   _zz_8;
  wire       [15:0]   _zz_9;
  wire       [15:0]   _zz_10;
  wire       [15:0]   _zz_11;
  wire       [15:0]   _zz_12;
  wire       [15:0]   _zz_13;
  wire       [15:0]   _zz_14;
  wire       [15:0]   _zz_15;
  wire       [15:0]   _zz_16;
  wire       [15:0]   _zz_17;
  wire       [15:0]   _zz_18;
  wire       [15:0]   _zz_19;
  wire       [15:0]   _zz_20;
  wire       [15:0]   _zz_21;
  wire       [15:0]   _zz_1;
  wire       [0:0]    _zz_2;

  assign _zz_6 = ($signed(a_real) + $signed(a_imag));
  assign _zz_7 = fixTo_dout;
  assign _zz_8 = ($signed(b_imag) - $signed(b_real));
  assign _zz_9 = ($signed(b_real) + $signed(b_imag));
  assign _zz_10 = _zz_11;
  assign _zz_11 = ($signed(_zz_12) >>> _zz_2);
  assign _zz_12 = _zz_13;
  assign _zz_13 = ($signed(_zz_1) - $signed(_zz_14));
  assign _zz_14 = _zz_15[15 : 0];
  assign _zz_15 = fixTo_2_dout;
  assign _zz_16 = _zz_17;
  assign _zz_17 = ($signed(_zz_18) >>> _zz_2);
  assign _zz_18 = _zz_19;
  assign _zz_19 = ($signed(_zz_1) + $signed(_zz_20));
  assign _zz_20 = _zz_21[15 : 0];
  assign _zz_21 = fixTo_1_dout;
  SInt32fixTo23_8_ROUNDTOINF fixTo (
    .din     (_zz_3[31:0]       ), //i
    .dout    (fixTo_dout[15:0]  )  //o
  );
  SInt32fixTo23_8_ROUNDTOINF fixTo_1 (
    .din     (_zz_4[31:0]         ), //i
    .dout    (fixTo_1_dout[15:0]  )  //o
  );
  SInt32fixTo23_8_ROUNDTOINF fixTo_2 (
    .din     (_zz_5[31:0]         ), //i
    .dout    (fixTo_2_dout[15:0]  )  //o
  );
  assign _zz_3 = ($signed(_zz_6) * $signed(b_real));
  assign _zz_1 = _zz_7[15 : 0];
  assign _zz_4 = ($signed(_zz_8) * $signed(a_real));
  assign _zz_5 = ($signed(_zz_9) * $signed(a_imag));
  assign _zz_2 = 1'b1;
  assign c_real = _zz_10[15 : 0];
  assign c_imag = _zz_16[15 : 0];

endmodule

//SInt32fixTo23_8_ROUNDTOINF replaced by SInt32fixTo23_8_ROUNDTOINF

//SInt32fixTo23_8_ROUNDTOINF replaced by SInt32fixTo23_8_ROUNDTOINF

module SInt32fixTo23_8_ROUNDTOINF (
  input      [31:0]   din,
  output     [15:0]   dout
);
  wire       [32:0]   _zz_9;
  wire       [32:0]   _zz_10;
  wire       [7:0]    _zz_11;
  wire       [24:0]   _zz_12;
  wire       [24:0]   _zz_13;
  wire       [32:0]   _zz_14;
  wire       [32:0]   _zz_15;
  wire       [32:0]   _zz_16;
  wire       [9:0]    _zz_17;
  wire       [8:0]    _zz_18;
  reg        [24:0]   _zz_1;
  wire       [31:0]   _zz_2;
  wire       [31:0]   _zz_3;
  wire       [31:0]   _zz_4;
  wire       [32:0]   _zz_5;
  wire       [31:0]   _zz_6;
  reg        [24:0]   _zz_7;
  reg        [15:0]   _zz_8;

  assign _zz_9 = {_zz_4[31],_zz_4};
  assign _zz_10 = {_zz_3[31],_zz_3};
  assign _zz_11 = _zz_5[7 : 0];
  assign _zz_12 = _zz_5[32 : 8];
  assign _zz_13 = 25'h0000001;
  assign _zz_14 = ($signed(_zz_15) + $signed(_zz_16));
  assign _zz_15 = {_zz_6[31],_zz_6};
  assign _zz_16 = {_zz_2[31],_zz_2};
  assign _zz_17 = _zz_1[24 : 15];
  assign _zz_18 = _zz_1[23 : 15];
  assign _zz_2 = {{24'h0,1'b1},7'h0};
  assign _zz_3 = {25'h1ffffff,7'h0};
  assign _zz_4 = din[31 : 0];
  assign _zz_5 = ($signed(_zz_9) + $signed(_zz_10));
  assign _zz_6 = din[31 : 0];
  always @ (*) begin
    if((_zz_11 != 8'h0))begin
      _zz_7 = ($signed(_zz_12) + $signed(_zz_13));
    end else begin
      _zz_7 = _zz_5[32 : 8];
    end
  end

  always @ (*) begin
    if(_zz_5[32])begin
      _zz_1 = _zz_7;
    end else begin
      _zz_1 = (_zz_14 >>> 8);
    end
  end

  always @ (*) begin
    if(_zz_1[24])begin
      if((! (_zz_17 == 10'h3ff)))begin
        _zz_8 = 16'h8000;
      end else begin
        _zz_8 = _zz_1[15 : 0];
      end
    end else begin
      if((_zz_18 != 9'h0))begin
        _zz_8 = 16'h7fff;
      end else begin
        _zz_8 = _zz_1[15 : 0];
      end
    end
  end

  assign dout = _zz_8;

endmodule
