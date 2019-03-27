import ammonite.ops._

val pubFile = pwd / "_data" / "publications.bib"

def entries(s: String) =
  s.split("@article").tail.map{x => "@article"+x}.toVector

val mrReg = "MR[0-9]+".r

def mrNum(s: String) = mrReg.findFirstIn(s).get.drop(2).toInt

def entryMap(s: String) =
  entries(s).map(x => mrNum(x) -> x).toMap

def view(m: Map[Int, String]) =
  m.toVector.sortBy{case (n, _) => -n}.map(_._2).mkString("")

var emap = entryMap(read.lines(pubFile).mkString("\n"))

def update(f: Path) = {
  val m = entryMap(read.lines(f).mkString("\n"))
  emap = emap ++ m
}

import $file.bib2yaml

def save() = {
  write.over(pubFile, view(emap))
  bib2yaml.run
}
