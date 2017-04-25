#!/bin/bash
# suppose $1 is the path that contains a series of model which we are going to verify

cd $1

# engine=backwards
engine=$2

for name in $(ls *.model)
do
	echo "-------------------------------------------------------------------------------"
	echo -e "model checking $1 ${name} : started at $(date)"
	# find the index
	n=${name/.model}

	case $1 in
		*"robot"*)
			echo -e "(this is a robot navigation model)"
			succqindex=3
			trapqindex=4
			;;
		*)
			echo -e "(this is a task complete model)"
			succqindex=$[10#$n + 1]
			trapqindex=$[10#$n + 2]
			;;
	esac

	echo "" > temp.prop
	echo "// maximum probability of success" >> temp.prop
	echo "Pmax=? [F q = $succqindex]" >> temp.prop
	echo "// minimum probability of success" >> temp.prop
	echo "Pmin=? [F q = $succqindex]" >> temp.prop
	echo "// maximum probability of being trapped" >> temp.prop
	echo "Pmax=? [F q = $trapqindex]" >> temp.prop
	echo "// minimum probability of being trapped" >> temp.prop
	echo "Pmin=? [F q = $trapqindex]" >> temp.prop

	# cat temp.prop
	prism $name temp.prop -ptamethod $engine > ${name}.log
	echo "finished at $(date)."
done
