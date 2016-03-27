ins= open("quote_file", "r")
f = open("quote_file_gen","w") 
array = []
for linef in ins:
	line=linef.split("\t")[3]
	ln=len(line)
	oval=ord(line[ln-1])
	if((oval>=65 and oval<=90) or (oval>=97 and oval<=122)) :
		newl=linef.replace(line, line+".")
		f.write(newl)
	else:
		#print (str(oval) + "  "+line[ln-1])
		f.write(linef)
		array.append(line[ln-1])
f.close()
print (array)
print (len(array))




