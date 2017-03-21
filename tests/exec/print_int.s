	.text
	.globl main
print_int:
	subq $16, %rsp
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rax
	movq $10, %rdi
	cqto
	idivq %rdi
	movq %rax, 8(%rsp)
	movq 0(%rsp), %rdi
	cmpq $9, %rdi
	jg L11
L9:
	movq 0(%rsp), %rdi
	movq $10, %rsi
	movq 8(%rsp), %rcx
	imulq %rcx, %rsi
	subq %rsi, %rdi
	addq $48, %rdi
	call putchar
	movq $0, %rax
	addq $16, %rsp
	ret
L11:
	movq 8(%rsp), %rdi
	call print_int
	jmp L9
main:
	movq $42, %rdi
	call print_int
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
	.data
