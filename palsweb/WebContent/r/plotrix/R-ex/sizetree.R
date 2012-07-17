### Name: sizetree
### Title: Display a hierarchical breakdown of disjunct categories
### Aliases: sizetree
### Keywords: misc

### ** Examples

 cat1<-sample(c("None","Low","Medium","High"),40,TRUE)
 cat2<-sample(c("None","Low","Medium","High"),40,TRUE)
 cat3<-sample(c("None","Low","Medium","High"),40,TRUE)
 hcats<-data.frame(cat1,cat2,cat3)
 bhcol<-list(c("#ff8080","#dddd80","#80ff80","#8080ff"),
  c("red","green","lightblue","yellow"),
  c("#ffffff","#bbbbbb","#999999","#666666"))
 sizetree(hcats,col=bhcol,main="Hierarchical count chart")



