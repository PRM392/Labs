import 'package:flutter/material.dart';
import 'calculator_page.dart';
import 'random_number_page.dart';

void main() {
  runApp(const MyApp());
}

/// Widget gốc của ứng dụng
class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Lab 16',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: const HomeTabPage(),
    );
  }
}

/// Màn hình có bottom tab: Calculator & Random Number
class HomeTabPage extends StatefulWidget {
  const HomeTabPage({super.key});

  @override
  State<HomeTabPage> createState() => _HomeTabPageState();
}

class _HomeTabPageState extends State<HomeTabPage> {
  int _currentIndex = 0;

  final List<Widget> _pages = const [
    CalculatorPage(),
    RandomNumberPage(),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _pages[_currentIndex],
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _currentIndex,
        onTap: (index) {
          setState(() {
            _currentIndex = index;
          });
        },
        items: const [
          BottomNavigationBarItem(
            icon: Icon(Icons.calculate_outlined),
            label: 'Calculator',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.casino_outlined),
            label: 'Random',
          ),
        ],
      ),
    );
  }
}
