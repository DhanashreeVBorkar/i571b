%-*- mode: prolog; -*-


% An employee is represented using the structure
% employee(Name, Age, Department, Salary).

% List of employees used for testing
employees([ employee(tom, 33, cs, 85000.00),
	    employee(joan, 23, ece, 110000.00),
	    employee(bill, 29, cs, 69500.00),
	    employee(john, 28, me, 58200.00),
	    employee(sue, 19, cs, 22000.00)
	  ]).

%%% #1 10-points
% dept_employees(Employees, Dept, DeptEmployees): Given a list
% Employees of employees and a department Dept match DeptEmployees
% to the subsequence of Employees having department Dept.
%dept_employees(Employees, Dept, DeptEmployees) :-
dept_employees([], _Department , []).

dept_employees([Head|Tail],Dept,[Head | Tail2]) :-
	employee(_Name,_Age,Department,_Salary) = Head,
	Department == Dept,
	dept_employees(Tail,Dept,Tail2).

dept_employees([Head|Tail],Dept,Tail2) :-
	employee(_Name,_Age,Department,_Salary) = Head,
	\+ Department == Dept,
	dept_employees(Tail,Dept,Tail2).
	
:- begin_tests(dept_employees, []).
test(dept_employees_cs, all(Zs =
			    [[ employee(tom, 33, cs, 85000.00),
			      employee(bill, 29, cs, 69500.00),
			      employee(sue, 19, cs, 22000.00)
			    ]])) :-
    employees(E),
    dept_employees(E, cs, Zs).

test(dept_employees_ece, all(Zs = [[ employee(joan, 23, ece, 110000.00) ]])) :-
    employees(E),
    dept_employees(E, ece, Zs).

test(dept_employees_ce, all(Zs = [[]])) :-
    employees(E),
    dept_employees(E, ce, Zs).
:- end_tests(dept_employees).


%===========================================================================

%%% #2 15-points
% employees_salary_sum(Employees, Sum): succeeds iff Sum matches sum
% of salaries of the employees in Employees.  Must be tail-recursive.
%employees_salary_sum(Employees, Sum) :- 'TODO'(Employees, Sum).

employees_salary_sum([], 0).

employees_salary_sum([Head|Tail], Sum) :- 
employee(_Name,_Age,_Department,Salary) = Head,
		employees_salary_sum(Tail, Acc1),
		Sum is Salary + Acc1.
	
:-begin_tests(employees_salary_sum).
test(empty) :-
    employees_salary_sum([], 0).
test(seq_salaries) :-
    Employees = [
	employee(_, _, _, 1), 
	employee(_, _, _, 2), 
	employee(_, _, _, 3), 
	employee(_, _, _, 4), 
	employee(_, _, _, 5)
    ],
    employees_salary_sum(Employees, 15).
test(all) :-
    employees(Employees),
    employees_salary_sum(Employees, 344700.00).
:-end_tests(employees_salary_sum).

%===========================================================================

%%% #3: 15-points
% list_access(Indexes, List, Z): Given a list Indexes containing
% 0-based indexes and a list List possibly containing lists nested to
% an abitrary depth, match Z with the element in List indexed
% successively by the indexes in Indexes. Match Z with the atom nil if
% there is no such element.
%list_access(Indexes, List, Z) :- 'TODO'(Indexes, List, Z).

%list_access([0|_],[Head|_],Head) :- !.

list_access([],List,Res):-
    Res=List.

list_access([N|_],List,Res):-
    not(nth0(N,List,_)),
    Res=nil.

list_access([N|Indexes],List,Res):-
    nth0(N,List,Element),
    list_access(Indexes,Element,Res).

%list_access([Index|Tail],[Lhead|Ltail],Z):-
%Index>0,
%Index1 is Index -1,
%list_access(Index1,Tail,Z).

:- begin_tests(list_access).
test(index_1, all(Z = [b])) :-
    list_access([1], [a, b, c], Z).
test(index_2, all(Z = [[c]])) :-
    list_access([2], [a, b, [c]], Z).
test(index_2_0, all(Z = [c])) :-
    list_access([2, 0], [a, b, [c]], Z).
test(index_2_1, all(Z = [nil])) :-
    list_access([2, 1], [a, b, [c]], Z).
test(index_3, all(Z = [nil])) :-
    list_access([3], [a, b, [c]], Z).
test(index_2_1, all(Z = [nil])) :-
    list_access([3], [a, b, [c]], Z).
test(index_empty, all(Z = [X])) :-
    X = [[1, 2, 3], [4, [5, 6, [8]]]],
    list_access([], X, Z).
test(index_and_list_empty, all(Z = [[]])) :-
    list_access([], [], Z).
test(list_empty, all(Z = [nil])) :-
    list_access([1], [], Z).
test(index_1_1_2, all(Z = [[8]])) :-
    X = [[1, 2, 3], [4, [5, 6, [8]]]],
    list_access([1, 1, 2], X, Z).
test(index_1_1_2_0, all(Z = [8])) :-
    X = [[1, 2, 3], [4, [5, 6, [8]]]],
    list_access([1, 1, 2, 0], X, Z).
test(index_0_1, all(Z = [nil])) :-
    list_access([0, 1], [[1]], Z).
:- end_tests(list_access).

%===========================================================================


%%% #4 15-points
% count_non_pairs(List, NNonPairs): NNonPairs matches the # of non-pairs
% in list List, including the non-pairs in any lists nested directly or
% indirectly in List.  Note that lists which are nested within a structure
% are not processed.
% The count will be the number of leaves in the tree corresponding to the
% list structure of List.
%count_non_pairs(List, NNonPairs):- 'TODO'(List, NNonPairs).

count_non_pairs([], 1).
count_non_pairs(List, 1):- List \= [_|_].
count_non_pairs([X|L], NNonPairs):- 
    count_non_pairs(L, NNPair),
    count_non_pairs(X,NNPairx), 
    NNonPairs is NNPair + NNPairx.


:- begin_tests(count_non_pairs).
test(empty, nondet) :-
    count_non_pairs([], 1).
test(unary_list, nondet) :-
    count_non_pairs([1], 2).
test(simple_list, nondet) :-
    count_non_pairs([1, 2, [], a, []], 6).
test(nested_list, nondet) :-
    count_non_pairs([[1, 2, 3], [[a], b, []]], 10).
test(nested_list_fail, fail) :-
    count_non_pairs([[1, 2, 3], [[a], b]], 10).
test(complex, nondet) :-
    count_non_pairs([[1, f([a, b, c]), h(1)], [[a], b]], 9).
test(complex_fail, fail) :-
    count_non_pairs([[1, f([a, b, c]), h(1)], [[a], b]], 8).
:- end_tests(count_non_pairs).

%===========================================================================

%%% #5 10-points
% divisible_by(Ints, N, Int): Int is an integer in list of integers Ints
% which is divisible by N.  Successive Int's are returned on backtracking
% in the order they occur within list Ints.
% Hint: use member/2 and the mod operator
%divisible_by(Ints, N, Int) :- 'TODO'(Ints, N, Int).

% List is empty, so return false
divisible_by([], _, _) :- false.
divisible_by(List, Num, Result) :- 
    % applying member/2
    member(X, List),
    X mod Num =:= 0,
    Result = X.


:- begin_tests(divisible_by).
test(empty, fail) :-
    divisible_by([], 2, _).
test(divisible_by_2, all(Int=[4, -8])) :-
    divisible_by([4, 7, -9, -8], 2, Int).
test(divisible_by_5, all(Int=[15, -25, 5])) :-
    divisible_by([4, 15, -25, -22, 5], 5, Int).
test(none_divisible_by_3, fail) :-
    divisible_by([4, 16, -25, -22, 5], 3, _Int).
:- end_tests(divisible_by).

%=================================
%%% #6 15-points
% re_match(Re, List): Regex Re matches all the symbols in List.
% A regex is represented in Prolog as follows:
%   A Prolog symbol Sym (for which atomic(Sym) is true) is a Prolog regex.
%   If A and B are Prolog regex's, then so is conc(A, B) representing AB.
%   If A and B are Prolog regex's, then so is alt(A, B) representing A|B.
%   If A is a Prolog regex, then so is kleene(A), representing A*.
%re_match(Re, List) :- 'TODO'(Re, List).


re_match(empty, []).

re_match(kleene(_),[]).

re_match(A, [Content]) :-
     A == Content.

re_match(alt(A, _), List) :-
    re_match(A, List).


re_match(alt(_, B), List) :- 
    re_match(B, List).      

re_match(conc(A, B), List) :-
    append(L1, L2, List),
    re_match(A, L1),
    re_match(B, L2). 

re_match(kleene(A), List) :- 
    append([Car|L1], L2, List),
    re_match(A, [Car|L1]),
    re_match(kleene(A), L2).

:- begin_tests(re_match).
test(single) :-
    re_match(a, [a]).
test(single_fail, fail) :-
    re_match(a, [b]).
test(conc) :-
    re_match(conc(a, b), [a, b]).
test(conc_fail, fail) :-
    re_match(conc(a, b), [a, c]).
test(alt1, nondet) :-
    re_match(alt(a, b), [a]).
test(alt2, nondet) :-
    re_match(alt(a, b), [b]).
test(alt_fail, fail) :-
    re_match(alt(a, b), [c]).
test(kleene_empty, nondet) :-
    re_match(kleene(a), []).
test(kleene_single, nondet) :-
    re_match(kleene(a), [a]).
test(kleene_multiple, nondet) :-
    re_match(kleene(a), [a, a, a, a]).
test(conc_kleene_sym, nondet) :-
    re_match(conc(kleene(a), b), [a, a, a, a, b]).
test(kleene_kleene0, nondet) :-
    re_match(conc(kleene(a), kleene(b)), [a, a, a, a]).
test(kleene_kleene, nondet) :-
    re_match(conc(kleene(a), kleene(b)), [a, a, a, a, b, b, b]).
test(kleene_kleene_fail, fail) :-
    re_match(conc(kleene(a), kleene(b)), [a, a, a, a, b, b, b, a]).
test(kleene_conc, nondet) :-
    re_match(kleene(conc(a, b)), [a, b, a, b, a, b]).
test(kleene_conc_fail, fail) :-
    re_match(kleene(conc(a, b)), [a, b, a, b, a, b, a]).
test(kleene_alt, nondet) :-
    re_match(kleene(alt(a, b)), [a, a, b, a, b, a, b, b]).
test(conc_kleene_conc, nondet) :-
    re_match(conc(a, conc(kleene(b), a)), [a, b, b, b, a]).
test(conc_kleene0_conc, nondet) :-
    re_match(conc(a, conc(kleene(b), a)), [a, a]).
test(conc_kleene_conc_fail, fail) :-
    re_match(conc(a, conc(kleene(b), a)), [a, b, b, b]).
test(complex1, nondet) :-
    re_match(conc(kleene(alt(a, b)), kleene(alt(0, 1))), [a,b,b,a,0,0,1,1]).
test(complex2, nondet) :-
    re_match(conc(kleene(alt(a, b)), kleene(alt(0, 1))), [0,0,1,1]).
test(complex_empty, nondet) :-
    re_match(conc(kleene(alt(a, b)), kleene(alt(0, 1))), []).
:- end_tests(re_match).

%=================================
%%% #7 20-points
% clausal_form(PrologRules, Form): given a non-empty list PrologRules
% of Prolog rules of the form Head or (Head :- Body), Form matches a
% logical conjunction (using the infix operator /\) of the clauses
% corresponding to each rule in PrologRules, where each clause is a
% disjunction (represented using the infix \/ operator) of literals with
% the prefix ~ operator used to indicate negative literals.
:- op(200, fx, ~). %declare ~ operator
%clausal_form(PrologRules, Form) :- 'TODO'(PrologRules, Form).

clausal_form([],0).

:- begin_tests(clausal_form).
test(single_head, all(Z = [p(a, b)])) :-
    clausal_form([p(a, b)], Z).
test(simple_rule, all(Z = [p(a, b) \/ ~q(a, b)])) :-
    clausal_form([(p(a, b) :- q(a, b))], Z).
test(rule_with_multi_body,
     all(Z = [p(a, b) \/ ~q(a, b) \/ ~r(a, b) \/ ~s(x)])) :-
    clausal_form([(p(a, b) :- q(a, b), r(a, b), s(x))], Z).
test(multi_rule, all(Z = [p(a, b) /\ q(x, y) /\ r(1)])) :-
    clausal_form([p(a, b), q(x, y), r(1)], Z).
test(complex, all(Z = [Clause1 /\ Clause2 /\ Clause3 /\ Clause4])) :-
    Rule1 = (p(a, b) :- q(b, c), r(a, b), s(x)),
    Clause1 = p(a, b) \/ ~q(b, c) \/ ~r(a, b) \/ ~s(x),
    Rule2 = (m(f(X)) :- n(f(X), Y), X is 2*Y),
    Clause2 = m(f(X)) \/ ~n(f(X), Y) \/ ~(X is 2*Y),
    Rule3 = append([], Xs, Xs),
    Clause3 = append([], Xs, Xs),
    Rule4 = (append([A|As], Ys, [A|Zs]) :- append(As, Ys, Zs)),
    Clause4 = append([A|As], Ys, [A|Zs]) \/ ~append(As, Ys, Zs),
    clausal_form([Rule1, Rule2, Rule3, Rule4], Z).
:- end_tests(clausal_form).
