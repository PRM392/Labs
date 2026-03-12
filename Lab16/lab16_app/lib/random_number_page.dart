import 'dart:math';
import 'package:flutter/material.dart';

/// ---------------- BÀI 2: RANDOM NUMBER APP ----------------

class RandomNumberPage extends StatefulWidget {
  const RandomNumberPage({super.key});

  @override
  State<RandomNumberPage> createState() => _RandomNumberPageState();
}

class _RandomNumberPageState extends State<RandomNumberPage> {
  final TextEditingController minController = TextEditingController();
  final TextEditingController maxController = TextEditingController();

  int randomNumber = 0;
  String? errorText;

  void generateNumber() {
    final String minText = minController.text.trim();
    final String maxText = maxController.text.trim();

    final int? minValue = int.tryParse(minText);
    final int? maxValue = int.tryParse(maxText);

    if (minText.isEmpty || maxText.isEmpty) {
      setState(() {
        errorText = 'Min và Max không được để trống';
      });
      return;
    }

    if (minValue == null || maxValue == null) {
      setState(() {
        errorText = 'Min và Max phải là số nguyên';
      });
      return;
    }

    if (minValue > maxValue) {
      setState(() {
        errorText = 'Min phải nhỏ hơn hoặc bằng Max';
      });
      return;
    }

    final random = Random();
    final int range = maxValue - minValue + 1;

    setState(() {
      errorText = null;
      randomNumber = minValue + random.nextInt(range);
    });
  }

  PreferredSizeWidget buildAppBar() {
    return AppBar(
      title: const Text('Random Number Generator'),
      centerTitle: true,
    );
  }

  Widget buildTitle() {
    return const Text(
      'Your Random Number',
      style: TextStyle(
        fontSize: 22,
        fontWeight: FontWeight.bold,
      ),
    );
  }

  Widget buildRangeInputs() {
    return Row(
      children: [
        Expanded(
          child: TextField(
            controller: minController,
            keyboardType: TextInputType.number,
            decoration: const InputDecoration(
              labelText: 'Min',
              border: OutlineInputBorder(),
            ),
          ),
        ),
        const SizedBox(width: 16),
        Expanded(
          child: TextField(
            controller: maxController,
            keyboardType: TextInputType.number,
            decoration: const InputDecoration(
              labelText: 'Max',
              border: OutlineInputBorder(),
            ),
          ),
        ),
      ],
    );
  }

  Widget buildNumberDisplay() {
    return Container(
      padding: const EdgeInsets.all(38),
      decoration: BoxDecoration(
        color: Colors.blue.shade100,
        borderRadius: BorderRadius.circular(20),
      ),
      child: Text(
        '$randomNumber',
        style: const TextStyle(
          fontSize: 50,
          fontWeight: FontWeight.bold,
          color: Colors.blue,
        ),
      ),
    );
  }

  Widget buildGenerateButton() {
    return ElevatedButton(
      onPressed: generateNumber,
      style: ElevatedButton.styleFrom(
        padding: const EdgeInsets.symmetric(horizontal: 40, vertical: 15),
      ),
      child: const Text(
        'Generate Number',
        style: TextStyle(fontSize: 18),
      ),
    );
  }

  Widget buildBody() {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          buildTitle(),
          const SizedBox(height: 24),
          buildRangeInputs(),
          if (errorText != null) ...[
            const SizedBox(height: 8),
            Text(
              errorText!,
              style: const TextStyle(
                color: Colors.red,
                fontSize: 14,
              ),
            ),
          ],
          const SizedBox(height: 24),
          buildNumberDisplay(),
          const SizedBox(height: 32),
          buildGenerateButton(),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: buildAppBar(),
      body: buildBody(),
    );
  }
}

