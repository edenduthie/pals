pyramid.plot             package:plotrix             R Documentation

_P_y_r_a_m_i_d _p_l_o_t

_D_e_s_c_r_i_p_t_i_o_n:

     Displays a pyramid (opposed horizontal bar) plot on the current
     graphics device.

_U_s_a_g_e:

      pyramid.plot(xy,xx,labels,top.labels=c("Male","Age","Female"),
       main="",laxlab=NULL,raxlab=NULL,unit="%",xycol,xxcol,gap=1,
       labelcex=1,mark.cat=NA,add=FALSE)

_A_r_g_u_m_e_n_t_s:

   xy,xx: Vectors of percentages (but see Details) both of which should
          total 100 if this is a population pyramid, and be of equal
          length.

  labels: Labels for the categories represented by each pair of bars.
          There should be a label for each xy or xx value, even if
          empty.

top.labels: The two categories represented on the left and right sides
          of the plot and a heading for the labels up the center.

    main: Optional title for the plot.

  laxlab: Optional labels for the left x axis ticks.

  raxlab: Optional labels for the right x axis ticks.

    unit: The label for the units of the plot.

xycol,xxcol: Color(s) for the left and right sets of bars. Both of
          these default to 1:length(labels).

     gap: One half of the space between the two sets of bars for the
          'labels' in user units.

labelcex: Expansion for the category labels.

mark.cat: If an integer equal to the index of one of the labels is
          passed, that label will have a rectangle drawn around it.

     add: Whether to add bars to an existing plot. Usually this
          involves overplotting a second set of bars, perhaps
          transparent.

_D_e_t_a_i_l_s:

     'pyramid.plot' is principally intended for population pyramids,
     although it can display other types of opposed bar charts with
     suitable modification of the arguments. If the user wants a
     different unit for the display, just change 'unit' accordingly.
     The default gap of  two units is usually satisfactory for the four
     to six percent range  of most bars on population pyramids.

     If 'xy' and 'xx' are matrices or data frames,  'pyramid.plot' will
     produce opposed stacked bars with the first  columns innermost. In
     this mode, colors are limited to one per column. The  stacked bar
     mode will in general not work with the 'add' method. Note  that
     the stacked bar mode can get very messy very quickly.

     The 'add' argument allows one or more sets of bars to be plotted
     on an existing plot. If these are not transparent, any bar that is
     shorter than the bar that overplots it will disappear. Only some
     graphic devices (e.g. 'pdf') will handle transparency.

     In order to add bars, the function cannot restore the initial
     margin values or the new bars will not plot properly. To
     automatically restore the plot margins, call the function as in
     the example.

_V_a_l_u_e:

     The return value of 'par("mar")' when the function was called.

_A_u_t_h_o_r(_s):

     Jim Lemon

_S_e_e _A_l_s_o:

     'rect'

_E_x_a_m_p_l_e_s:

      xy.pop<-c(3.2,3.5,3.6,3.6,3.5,3.5,3.9,3.7,3.9,3.5,3.2,2.8,2.2,1.8,
       1.5,1.3,0.7,0.4)
      xx.pop<-c(3.2,3.4,3.5,3.5,3.5,3.7,4,3.8,3.9,3.6,3.2,2.5,2,1.7,1.5,
       1.3,1,0.8)
      agelabels<-c("0-4","5-9","10-14","15-19","20-24","25-29","30-34",
       "35-39","40-44","45-49","50-54","55-59","60-64","65-69","70-74",
       "75-79","80-44","85+")
      xycol<-color.gradient(c(0,0,0.5,1),c(0,0,0.5,1),c(1,1,0.5,1),18)
      xxcol<-color.gradient(c(1,1,0.5,1),c(0.5,0.5,0.5,1),c(0.5,0.5,0.5,1),18)
      par(mar=pyramid.plot(xy.pop,xx.pop,labels=agelabels,
       main="Australian population pyramid 2002",xycol=xycol,xxcol=xxcol))
      # three column matrices
      avtemp<-c(seq(11,2,by=-1),rep(2:6,each=2),seq(11,2,by=-1))
      malecook<-matrix(avtemp+sample(-2:2,30,TRUE),ncol=3)
      femalecook<-matrix(avtemp+sample(-2:2,30,TRUE),ncol=3)
      # use a background color
      par(bg="#eedd55")
      # group by age
      agegrps<-c("0-10","11-20","21-30","31-40","41-50","51-60",
       "61-70","71-80","81-90","91+")
      oldmar<-pyramid.plot(malecook,femalecook,labels=agegrps,
       unit="Bowls per month",xycol=c("#ff0000","#eeee88","#0000ff"),
       xxcol=c("#ff0000","#eeee88","#0000ff"),
       top.labels=c("Males","Age","Females"),gap=3)
      # put a box around it
      box()
      # give it a title
      mtext("Porridge temperature by age and sex of cook",3,2,cex=1.5)
      # stick in a legend
      legend(par("usr")[1],11,c("Too hot","Just right","Too cold"),
       fill=c("#ff0000","#eeee88","#0000ff"))
      # don't forget to restore the margins and background
      par(mar=oldmar,bg="transparent")

