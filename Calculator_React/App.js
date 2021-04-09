
import React, { Component } from 'react';
import InputButton from './src/InputButton';
import Style from './src/Style';
import {View,
 Text,
 AppRegistry
 } from 'react-native';

 const inputButtons = [
  ['CE', 'C'],
  [1, 2, 3, '/'],
  [4, 5, 6, '*'],
  [7, 8, 9, '-'],
  [0, '.', '=', '+']
];
 
 class ReactCalculator extends Component {
      constructor(props) {
        super(props);
        
        this.state = {
          previousInputValue: 0,
          inputValue: 0,
          selectedSymbol: null
        }
    }

    render() {
      return (
          <View style={Style.rootContainer}>
              <View style={Style.displayContainer}>
                  <Text style={Style.displayText}>{this.state.inputValue}</Text>
              </View>
              <View style={Style.inputContainer}>
                  {this._renderInputButtons()}
              </View>
          </View>
      )
  }
_renderInputButtons() {
  let views = [];

  for (var r = 0; r < inputButtons.length; r ++) {
      let row = inputButtons[r];

      let inputRow = [];
      for (var i = 0; i < row.length; i ++) {
          let input = row[i];

          inputRow.push(
            <InputButton
                value={input}
                onPress={this._onInputButtonPressed.bind(this, input)}
                key={r + "-" + i}/>
        );
      }

      views.push(<View style={Style.inputRow} key={"row-" + r}>{inputRow}</View>)
  }

  return views;
}
_onInputButtonPressed(input) {
  switch (typeof input) {
      case 'number':
          return this._handleNumberInput(input)
      case 'string':
          return this._handleStringInput(input)
  }
}
_handleNumberInput(num) {
  let inputValue = (this.state.inputValue * 10) + num;

  this.setState({
      inputValue: inputValue
  })
}

_handleStringInput(str) {
  switch (str) {
      case '/':
      case '*':
      case '+':
      case '-':
          this.setState({
              selectedSymbol: str,
              previousInputValue: this.state.inputValue,
              inputValue: 0
          });
          break;
          case '=':
                let symbol = this.state.selectedSymbol,
                    inputValue = this.state.inputValue,
                    previousInputValue = this.state.previousInputValue;
                    console.log(inputValue)

                if (!symbol) {
                    return;
                }else if(inputValue == 0 || symbol == '/'){
                  this.setState({
                    inputValue: 'La division par 0 ne fonctionne pas',
                    selectedSymbol: null,
                    previousInputValue: 0,
                  })
                }else{
                  this.setState({
                    previousInputValue: 0,
                    inputValue: eval(previousInputValue + symbol + inputValue),
                    selectedSymbol: null
                });
                }

                
                break;
          case 'CE': 
                inputValue = 0;   
                this.state.previousInputValue = 0;
                this.setState({
                  inputValue: inputValue
                }) 
          case 'C':   
            inputValue = 0;   
            this.setState({
              inputValue: inputValue
            }) 
  }
}
}
 
 export default ReactCalculator