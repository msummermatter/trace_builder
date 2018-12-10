library(data.table)
library(ggplot2)

dt <- data.table(read.csv("r20x2.txt",header=F))

dt[,pid:=as.numeric(V5)] # data.table syntax
dt[,address:=as.numeric(V7)] # data.table syntax
dt[,time:=as.numeric(V4)]

dt2 <- dt#[pid==8093 & time >4]

dt2

dt[V7=="8093"]

ggplot(dt2,aes(x=time,y=address,color=factor(pid)))+
  geom_point()+
  scale_y_continuous("address")+
  scale_x_continuous("time")



