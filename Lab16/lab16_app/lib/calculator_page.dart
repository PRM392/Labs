import 'package:flutter/material.dart';

/// ---------------- BÀI 1: CALCULATOR APP ----------------

class CalculatorPage extends StatefulWidget {
  const CalculatorPage({super.key});

  @override
  State<CalculatorPage> createState() => _CalculatorPageState();
}

class _CalculatorPageState extends State<CalculatorPage> {
  final TextEditingController number1Controller = TextEditingController();
  final TextEditingController number2Controller = TextEditingController();

  double result = 0;

  void calculate(String operator) {
    final double num1 = double.tryParse(number1Controller.text) ?? 0;
    final double num2 = double.tryParse(number2Controller.text) ?? 0;

    setState(() {
      if (operator == '+') {
        result = num1 + num2;
      } else if (operator == '-') {
        result = num1 - num2;
      } else if (operator == '*') {
        result = num1 * num2;
      } else if (operator == '/') {
        result = num2 != 0 ? num1 / num2 : 0;
      }
    });
  }

  Widget _buildFirstInput() {
    return TextField(
      controller: number1Controller,
      keyboardType: TextInputType.number,
      decoration: const InputDecoration(
        labelText: 'Enter first number',
        border: OutlineInputBorder(),
      ),
    );
  }

  Widget _buildSecondInput() {
    return TextField(
      controller: number2Controller,
      keyboardType: TextInputType.number,
      decoration: const InputDecoration(
        labelText: 'Enter second number',
        border: OutlineInputBorder(),
      ),
    );
  }

  Widget _buildButton(String operator) {
    return ElevatedButton(
      onPressed: () => calculate(operator),
      child: Text(
        operator,
        style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
      ),
    );
  }

  Widget _buildOperatorButtons() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        _buildButton('+'),
        _buildButton('-'),
        _buildButton('*'),
        _buildButton('/'),
      ],
    );
  }

  Widget _buildResult() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Result:',
          style: TextStyle(fontSize: 18, fontWeight: FontWeight.w600),
        ),
        const SizedBox(height: 8),
        Container(
          width: double.infinity,
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: Colors.blue.shade50,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Text(
            '$result',
            style: const TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
              color: Colors.blue,
            ),
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Simple Calculator'),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            _buildFirstInput(),
            const SizedBox(height: 16),
            _buildSecondInput(),
            const SizedBox(height: 20),
            _buildOperatorButtons(),
            const SizedBox(height: 30),
            _buildResult(),
          ],
        ),
      ),
    );
  }
}

