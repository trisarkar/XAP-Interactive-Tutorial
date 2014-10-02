#!/bin/bash
LIGHT_GREEN='\x1B[1;32m'
LIGHT_BLUE='\x1B[1;34m'
CYAN='\x1B[1;36m'
NC='\x1B[0m' # No Color
RED='\x1B[0;31m'

if [ -z "$GS_HOME" ]; then
    echo -e "${RED}GS_HOME environment variable is not set${NC}"
	read -p "Press any key to continue . . ."
	exit
fi

if [[ ! -f "$GS_HOME/tools/groovy/bin/groovy" ]]; then
    echo -e "${RED}GS_HOME environment variable is not configured properly or groovy cannot be found, please make sure it points to XAP home directory ($GS_HOME)${NC}"
	read -p "Press any key to continue . . ."
	exit
fi

trap '' 2

while true; do
clear
echo ""
echo ""
echo -e """${CYAN}ooooooo  ooooo       .o.       ooooooooo."""
echo -e """ \`8888    d8'       .888.      \`888   \`Y88."""
echo -e """   Y888..8P        .8\"888.      888   .d88'"""
echo -e """    \`8888'        .8' \`888.     888ooo88P'"""
echo -e """   .8PY888.      .88ooo8888.    888"""
echo -e """  d8'  \`888b    .8'     \`888.   888"""
echo -e """o888o  o88888o o88o     o8888o o888o${NC}"""
echo ""
echo "XAP Interactive Tutorial"
echo "Choose one of the options for tutorial bellow:"
echo -e "${LIGHT_GREEN}1]${NC} XAP Demo - Write/Read to/from myDataGrid space"
echo -e "${LIGHT_GREEN}2]${NC} XAP 10 New API - Query Aggregation, Custom Change"
echo -e "${LIGHT_GREEN}3]${NC} XAP Interactive Shell"
echo -e "${LIGHT_GREEN}0]${NC} Exit"
echo -n "Your choice: "

while true; do
read case;

SYSTEM_PROPERTIES="-Djava.rmi.server.hostname=${NIC_ADDR}"
if [ -n "$LRMI_COMM_MIN_PORT" ] && [ -n "$LRMI_COMM_MAX_PORT" ]; then
SYSTEM_PROPERTIES="${SYSTEM_PROPERTIES} -Dcom.gs.transport_protocol.lrmi.bind-port=$LRMI_COMM_MIN_PORT-$LRMI_COMM_MAX_PORT"
fi


case "$case" in
1 ) $GS_HOME/tools/groovy/bin/groovy ${SYSTEM_PROPERTIES} XAPDemoScript.groovy ; break ;;
2 ) $GS_HOME/tools/groovy/bin/groovy ${SYSTEM_PROPERTIES} XAP10NewAPI.groovy ; break ;;
3 ) clear ; $GS_HOME/tools/groovy/bin/groovysh ${SYSTEM_PROPERTIES} ; break ;;
0 ) break ;;
esac
echo -n "Invalid option, please try again: "

done

if [ "$case" = "0" ]; then
break;
fi

read -p "Press any key to continue . . ."

done

trap 2
echo -e "${CYAN}Thank you for using XAP Interactive Tutorial${NC}"
