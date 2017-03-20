	.text
	.globl main
get:
	movq %rsi, %rcx
	cmpq $0, %rcx
	je L8
	movq 8(%rdi), %rdi
	subq $1, %rsi
	call get
L1:
	ret
L8:
	movq 0(%rdi), %rax
	jmp L1
set:
	movq %rsi, %rcx
	cmpq $0, %rcx
	je L21
	movq 8(%rdi), %rdi
	subq $1, %rsi
	call set
	movq %rax, %rdx
L12:
	movq %rdx, %rax
	ret
L21:
	movq %rdx, 0(%rdi)
	jmp L12
create:
	subq $16, %rsp
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	cmpq $0, %rdi
	je L38
	movq $16, %rdi
	call sbrk
	movq %rax, 8(%rsp)
	movq $0, %rcx
	movq 8(%rsp), %rdi
	movq %rcx, 0(%rdi)
	movq 0(%rsp), %rdi
	subq $1, %rdi
	call create
	movq 8(%rsp), %rdi
	movq %rax, 8(%rdi)
	movq 8(%rsp), %rax
L25:
	addq $16, %rsp
	ret
L38:
	movq $0, %rax
	jmp L25
print_row:
	subq $24, %rsp
	movq %rdi, 0(%rsp)
	movq %rsi, 8(%rsp)
	movq $0, %rdi
	movq %rdi, 16(%rsp)
L61:
	movq 16(%rsp), %rcx
	movq 8(%rsp), %rdi
	cmpq %rdi, %rcx
	jle L58
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $24, %rsp
	ret
L58:
	movq 0(%rsp), %rdi
	movq 16(%rsp), %rsi
	call get
	cmpq $0, %rax
	jne L51
	movq $46, %rdi
	call putchar
L49:
	movq 16(%rsp), %rdi
	addq $1, %rdi
	movq %rdi, 16(%rsp)
	jmp L61
L51:
	movq $42, %rdi
	call putchar
	jmp L49
mod7:
	movq %rbx, %rcx
	movq $7, %r8
	movq %rdi, %rax
	movq $7, %rsi
	movq %rdx, %rbx
	movq $0, %rdx
	idivq %rsi
	imulq %rax, %r8
	subq %r8, %rdi
	movq %rdi, %rax
	movq %rcx, %rbx
	ret
compute_row:
	subq $24, %rsp
	movq %rdi, 0(%rsp)
	movq %rsi, 8(%rsp)
L95:
	movq 8(%rsp), %rdi
	cmpq $0, %rdi
	jg L93
	movq 0(%rsp), %rdi
	movq $0, %rsi
	movq $1, %rdx
	call set
	movq $0, %rax
	addq $24, %rsp
	ret
L93:
	movq 0(%rsp), %rdi
	movq 8(%rsp), %rsi
	call get
	movq %rax, 16(%rsp)
	movq 0(%rsp), %rdi
	movq 8(%rsp), %rsi
	subq $1, %rsi
	call get
	addq %rax, 16(%rsp)
	movq 16(%rsp), %rdi
	call mod7
	movq %rax, %rdx
	movq 0(%rsp), %rdi
	movq 8(%rsp), %rsi
	call set
	movq 8(%rsp), %rdi
	subq $1, %rdi
	movq %rdi, 8(%rsp)
	jmp L95
pascal:
	subq $24, %rsp
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	addq $1, %rdi
	call create
	movq %rax, 8(%rsp)
	movq $0, %rdi
	movq %rdi, 16(%rsp)
L116:
	movq 16(%rsp), %rdi
	movq 0(%rsp), %rcx
	cmpq %rcx, %rdi
	jl L113
	movq $0, %rax
	addq $24, %rsp
	ret
L113:
	movq 8(%rsp), %rdi
	movq 16(%rsp), %rsi
	movq $0, %rdx
	call set
	movq 8(%rsp), %rdi
	movq 16(%rsp), %rsi
	call compute_row
	movq 8(%rsp), %rdi
	movq 16(%rsp), %rsi
	call print_row
	movq 16(%rsp), %rdi
	addq $1, %rdi
	movq %rdi, 16(%rsp)
	jmp L116
main:
	movq $42, %rdi
	call pascal
	movq $0, %rax
	ret
	.data
